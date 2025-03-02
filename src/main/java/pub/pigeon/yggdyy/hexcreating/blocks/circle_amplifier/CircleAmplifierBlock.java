package pub.pigeon.yggdyy.hexcreating.blocks.circle_amplifier;

import at.petrak.hexcasting.api.casting.circles.ICircleComponent;
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;
import pub.pigeon.yggdyy.hexcreating.libs.FieldHacker;
import pub.pigeon.yggdyy.hexcreating.mixins.CircleCastInvAccessor;

import java.util.EnumSet;

public class CircleAmplifierBlock extends DirectionalKineticBlock implements IBE<CircleAmplifierBlockEntity>, ICircleComponent {
    public static final BooleanProperty ENERGIZED = BooleanProperty.of("energized");

    public CircleAmplifierBlock(Settings properties) {
        super(properties);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ENERGIZED);
        super.appendProperties(pBuilder);
    }
    @Override
    public boolean hasShaftTowards(WorldView world, BlockPos pos, BlockState state, Direction face) {
        return face.getDirection().equals(state.get(FACING).getDirection().getOpposite());
    }

    @Override
    public ControlFlow acceptControlFlow(CastingImage imageIn, CircleCastEnv env, Direction enterDir, BlockPos pos, BlockState bs, ServerWorld world) {
        var exitDirsSet = this.possibleExitDirections(pos, bs, world);
        exitDirsSet.remove(enterDir.getOpposite());
        var exitDirs = exitDirsSet.stream().map((dir) -> this.exitPositionFromDirection(pos, dir));

        //world.getPlayers().get(0).sendMessage(Text.literal(((CircleCastInvAccessor) env).getExecState().bounds.toString()));
        var execState = ((CircleCastInvAccessor) env).getExecState();
        int extendLength = (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof CircleAmplifierBlockEntity be)? (int)Math.abs(be.getSpeed()) / 4 : 0;
        double x = Math.min(pos.getX() - extendLength, execState.bounds.minX),
                y = Math.min(pos.getY() - extendLength, execState.bounds.minY),
                z = Math.min(pos.getZ() - extendLength, execState.bounds.minY),
                X = Math.max(pos.getX() + extendLength, execState.bounds.maxX),
                Y = Math.max(pos.getY() + extendLength, execState.bounds.maxY),
                Z = Math.max(pos.getZ() + extendLength, execState.bounds.maxZ);
        //((CircleExecutionStateAccessor) execState).setBounds(new Box(x, y, z, X, Y, Z));
        try {
            FieldHacker.hackFinalField(execState, "bounds", new Box(x, y, z, X, Y, Z));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //world.getPlayers().get(0).sendMessage(Text.literal(((CircleCastInvAccessor) env).getExecState().bounds.toString()));
        return new ControlFlow.Continue(imageIn, exitDirs.toList());
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
    public BlockState startEnergized(BlockPos pos, BlockState bs, World world) {
        var newState = bs.with(ENERGIZED, true);
        world.setBlockState(pos, newState);

        return newState;
    }
    @Override
    public boolean isEnergized(BlockPos pos, BlockState bs, World world) {
        return bs.get(ENERGIZED);
    }
    @Override
    public BlockState endEnergized(BlockPos pos, BlockState bs, World world) {
        var newState = bs.with(ENERGIZED, false);
        world.setBlockState(pos, newState);
        return newState;
    }
    @Override
    public boolean hasComparatorOutput(BlockState pState) {
        return true;
    }
    @Override
    public int getComparatorOutput(BlockState pState, World pLevel, BlockPos pPos) {
        return pState.get(ENERGIZED) ? 15 : 0;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.get(FACING).getAxis();
    }

    @Override
    public Class<CircleAmplifierBlockEntity> getBlockEntityClass() {
        return CircleAmplifierBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CircleAmplifierBlockEntity> getBlockEntityType() {
        return ModBlockEntities.CIRCLE_AMPLIFIER;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return super.getPlacementState(context).with(ENERGIZED, false);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        return switch (blockState.get(FACING)) {
            case DOWN -> VoxelShapes.cuboid(0, 0.75, 0, 1, 1, 1);
            case UP -> VoxelShapes.cuboid(0, 0, 0, 1, 0.25, 1);
            case WEST -> VoxelShapes.cuboid(0.75, 0, 0, 1, 1, 1);
            case EAST -> VoxelShapes.cuboid(0, 0, 0, 0.25, 1, 1);
            case SOUTH -> VoxelShapes.cuboid(0, 0, 0, 1, 1, 0.25);
            case NORTH -> VoxelShapes.cuboid(0, 0, 0.75, 1, 1, 1);
        };
    }
}
