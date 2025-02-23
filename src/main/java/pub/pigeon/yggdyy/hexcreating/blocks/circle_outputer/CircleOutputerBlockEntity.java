package pub.pigeon.yggdyy.hexcreating.blocks.circle_outputer;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;

public class CircleOutputerBlockEntity extends BlockEntity {
    public CircleOutputerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CIRCLE_OUTPUTER, blockPos, blockState);
    }
}
