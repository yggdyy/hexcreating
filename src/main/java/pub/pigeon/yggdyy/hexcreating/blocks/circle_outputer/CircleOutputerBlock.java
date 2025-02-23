package pub.pigeon.yggdyy.hexcreating.blocks.circle_outputer;

import at.petrak.hexcasting.api.block.circle.BlockCircleComponent;
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraft.world.World;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaNetProcessor;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaPack;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaTerminal;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;

import java.util.EnumSet;
import java.util.Locale;

public class CircleOutputerBlock extends BlockCircleComponent implements IWrenchable, IBE<CircleOutputerBlockEntity> {
    public static final EnumProperty<OutputTypes> OUTPUT_TYPE = EnumProperty.of("output_type", OutputTypes.class);

    public CircleOutputerBlock(Settings p_49795_) {
        super(p_49795_);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(OUTPUT_TYPE);
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
        var exitDirsSet = this.possibleExitDirections(pos, bs, world);
        exitDirsSet.remove(enterDir.getOpposite());
        var exitDirs = exitDirsSet.stream().map((dir) -> this.exitPositionFromDirection(pos, dir));
        if(imageIn.getStack().size() == 0) return new ControlFlow.Continue(imageIn, exitDirs.toList());
        var type = world.getBlockState(pos).get(OUTPUT_TYPE);
        var iotas = imageIn.getStack();
        if(world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof CircleOutputerBlockEntity be) {
            if (type == OutputTypes.SILENCE) {
                //currently nothing
            } else if (type == OutputTypes.SINGLE) {
                IotaTerminal terminal = new IotaTerminal(be, 0);
                IotaPack pack = IotaPack.createNew(iotas.get(iotas.size() - 1));
                sendIotaPacks(pack, terminal, world);
                //world.getPlayers().get(0).sendMessage(Text.literal("yggdyy_!"));
            } else if (type == OutputTypes.MULTIPLE) {
                IotaTerminal terminal = new IotaTerminal(be, 0);
                IotaPack pack = IotaPack.createNew(new ListIota(iotas));
                sendIotaPacks(pack, terminal, world);
            }
        }
        return new ControlFlow.Continue(imageIn, exitDirs.toList());
    }
    private void sendIotaPacks(IotaPack pack, IotaTerminal terminal, ServerWorld world) {
        for(var d : EnumSet.allOf(Direction.class)) {
            var fPos = terminal.blockEntity.getPos();
            var tPos = fPos.add(d.getVector());
            IotaNetProcessor.trySendIotaPack(pack.copyWithSameId(), terminal, tPos, world);
        }
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
        context.getWorld().setBlockState(context.getBlockPos(), state.with(OUTPUT_TYPE, state.get(OUTPUT_TYPE).next()));
        playRotateSound(context.getWorld(), context.getBlockPos());
        return ActionResult.SUCCESS;
    }

    @Override
    public Class<CircleOutputerBlockEntity> getBlockEntityClass() {
        return CircleOutputerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CircleOutputerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.CIRCLE_OUTPUTER;
    }

    public enum OutputTypes implements StringIdentifiable {
        SILENCE,
        SINGLE,
        MULTIPLE;
        @Override
        public String asString() {
            return this.name().toLowerCase(Locale.ROOT);
        }
        public OutputTypes next() {
            return switch (this) {
                case SILENCE -> SINGLE;
                case SINGLE -> MULTIPLE;
                case MULTIPLE -> SILENCE;
            };
        }
    }
}
