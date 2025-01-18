package pub.pigeon.yggdyy.hexcreating.blocks.iotawriter;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;
import pub.pigeon.yggdyy.hexcreating.blocks.iotareader.IotaReaderBlockEntity;

public class IotaWriteBlock extends HorizontalKineticBlock implements IBE<IotaWriteBlockEntity> {

    public IotaWriteBlock(Settings properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.get(HORIZONTAL_FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(WorldView world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.get(HORIZONTAL_FACING).getAxis();
    }


    @Override
    public Class<IotaWriteBlockEntity> getBlockEntityClass() {
        return IotaWriteBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends IotaWriteBlockEntity> getBlockEntityType() {
        return ModBlockEntities.IOTA_WRITER;
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }
}
