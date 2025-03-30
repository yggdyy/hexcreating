package pub.pigeon.yggdyy.hexcreating.blocks.train_gate;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;

public class TrainGateCoreBlock extends Block implements IBE<TrainGateCoreBlockEntity> {
    public TrainGateCoreBlock(Settings settings) {
        super(settings);
    }

    @Override
    public Class<TrainGateCoreBlockEntity> getBlockEntityClass() {
        return TrainGateCoreBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TrainGateCoreBlockEntity> getBlockEntityType() {
        return ModBlockEntities.TRAIN_GATE_CORE;
    }

    @Override
    public void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
        if(world != null && !world.isClient && world.getBlockEntity(blockPos) instanceof TrainGateCoreBlockEntity be) {
            be.tryDestroyGate();
        }
        super.onBreak(world, blockPos, blockState, playerEntity);
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos blockPos, Explosion explosion) {
        if(world != null && !world.isClient && world.getBlockEntity(blockPos) instanceof TrainGateCoreBlockEntity be) {
            be.tryDestroyGate();
        }
        super.onDestroyedByExplosion(world, blockPos, explosion);
    }
}
