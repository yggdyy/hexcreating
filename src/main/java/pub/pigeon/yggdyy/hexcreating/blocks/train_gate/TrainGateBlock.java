package pub.pigeon.yggdyy.hexcreating.blocks.train_gate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;

public class TrainGateBlock extends Block {
    public TrainGateBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.HORIZONTAL_AXIS);
    }

    @Nullable
    public BlockPos getCorePos(World world, BlockPos pos) {
        Direction.Axis axis = world.getBlockState(pos).get(Properties.HORIZONTAL_AXIS);
        int bottomY = pos.getY();
        while(world.getBlockState(new BlockPos(pos.getX(), bottomY, pos.getZ())).isOf(ModBlocks.INSTANCE.getTRAIN_GATE())) --bottomY;
        if(axis.equals(Direction.Axis.X)) {
            for(int i = pos.getX() - 10; i <= pos.getX() + 10; ++i) {
                BlockPos nowPos = new BlockPos(i, bottomY, pos.getZ());
                if(world.getBlockState(nowPos).isOf(ModBlocks.INSTANCE.getTRAIN_GATE_CORE()) && world.getBlockEntity(nowPos) instanceof TrainGateCoreBlockEntity be) {
                    if(be.isInGate(pos)) return nowPos;
                }
            }
        } else if(axis.equals(Direction.Axis.Z)) {
            for(int k = pos.getZ() - 10; k <= pos.getZ() + 10; ++k) {
                BlockPos nowPos = new BlockPos(pos.getX(), bottomY, k);
                if(world.getBlockState(nowPos).isOf(ModBlocks.INSTANCE.getTRAIN_GATE_CORE()) && world.getBlockEntity(nowPos) instanceof TrainGateCoreBlockEntity be) {
                    if(be.isInGate(pos)) return nowPos;
                }
            }
        }
        return null;
    }
    private void onDestroyed(World world, BlockPos pos) {
        if(world != null && !world.isClient) {
            BlockPos corePos = getCorePos(world, pos);
            if(corePos != null) {
                ((TrainGateCoreBlockEntity) world.getBlockEntity(corePos)).tryDestroyGate();
            }
        }
    }
    @Override
    public void onDestroyedByExplosion(World world, BlockPos blockPos, Explosion explosion) {
        onDestroyed(world, blockPos);
        super.onDestroyedByExplosion(world, blockPos, explosion);
    }
    @Override
    public void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
        onDestroyed(world, blockPos);
        super.onBreak(world, blockPos, blockState, playerEntity);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        switch ((Direction.Axis)blockState.get(Properties.HORIZONTAL_AXIS)) {
            case Z:
                return Block.createCuboidShape(7.0, 0.0, 0.0, 9.0, 16.0, 16.0);
            case X:
            default:
                return Block.createCuboidShape(0.0, 0.0, 7.0, 16.0, 16.0, 9.0);
        }
    }
}
