package pub.pigeon.yggdyy.hexcreating.blocks.circle_inputer;

import at.petrak.hexcasting.api.block.circle.BlockCircleComponent;
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.iota.Iota;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

public class CircleInputerBlock extends BlockCircleComponent implements IBE<CircleInputerBlockEntity>, IWrenchable {
    //private static final Logger LOGGER = LoggerFactory.getLogger(CircleInputerBlock.class);

    public static final EnumProperty<InputTypes> INPUT_TYPE = EnumProperty.of("input_type", InputTypes.class);

    public CircleInputerBlock(Settings p_49795_) {
        super(p_49795_);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(INPUT_TYPE);
    }

    @Override
    public Class<CircleInputerBlockEntity> getBlockEntityClass() {
        return CircleInputerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CircleInputerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.CIRCLE_INPUTER;
    }

    @Override
    public Direction normalDir(BlockPos pos, BlockState bs, World world, int recursionLeft) {
        return Direction.UP;
    }

    @Override
    public float particleHeight(BlockPos pos, BlockState bs, World world) {
        return 0.6f;
    }

    @Override
    public ControlFlow acceptControlFlow(CastingImage imageIn, CircleCastEnv env, Direction enterDir, BlockPos pos, BlockState bs, ServerWorld world) {
        if(world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof CircleInputerBlockEntity be) {
            //world.getPlayers().get(0).sendMessage(Text.literal(imageIn.getStack().size() + ""));
            var exitDirsSet = this.possibleExitDirections(pos, bs, world);
            exitDirsSet.remove(enterDir.getOpposite());
            var exitDirs = exitDirsSet.stream().map((dir) -> this.exitPositionFromDirection(pos, dir));
            if(be.iotas == null || be.iotas.size() == 0) return new ControlFlow.Continue(imageIn, exitDirs.toList());
            List<Iota> resIotas = new ArrayList<>(imageIn.getStack());
            resIotas.addAll(be.iotas);
            CastingImage resImg = imageIn.copy(resIotas, imageIn.getParenCount(), imageIn.getParenthesized(), imageIn.getEscapeNext(), imageIn.getOpsConsumed(), imageIn.getUserData());
            //HexcreatingMain.LOGGER.debug(imageIn.getStack().size() + "");
           // world.getPlayers().get(0).sendMessage(resImg.getStack().get(0).display());
            return new ControlFlow.Continue(resImg, exitDirs.toList());
        }
        return new ControlFlow.Stop();
    }

    @Override
    public boolean canEnterFromDirection(Direction enterDir, BlockPos pos, BlockState bs, ServerWorld world) {
        return true;
    }

    @Override
    public EnumSet<Direction> possibleExitDirections(BlockPos pos, BlockState bs, World world) {
        return EnumSet.allOf(Direction.class);
    }

    @Override
    public ActionResult onWrenched(BlockState state, ItemUsageContext context) {
        context.getWorld().setBlockState(context.getBlockPos(), state.with(INPUT_TYPE, state.get(INPUT_TYPE).next()));
        playRotateSound(context.getWorld(), context.getBlockPos());
        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        return VoxelShapes.fullCube();
    }

    public enum InputTypes implements StringIdentifiable {
        REJECT, //reject all iota packs
        ALWAYS_SINGLE, //always use the received iota pack as a single iota, even if it contains a list
        TRY_MULTIPLE // if the iota pack contains a list iota, then break it into several (sometimes, only 1) iotas
        ;
        @Override
        public String asString() {
            return this.name().toLowerCase(Locale.ROOT);
        }
        public InputTypes next() {
            return switch (this) {
                case REJECT -> ALWAYS_SINGLE;
                case ALWAYS_SINGLE -> TRY_MULTIPLE;
                case TRY_MULTIPLE -> REJECT;
            };
        }
    }
}
