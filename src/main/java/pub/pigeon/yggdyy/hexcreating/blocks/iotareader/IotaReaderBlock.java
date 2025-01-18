package pub.pigeon.yggdyy.hexcreating.blocks.iotareader;

import com.google.common.collect.ImmutableMap;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;

import java.util.List;
import java.util.function.Function;

public class IotaReaderBlock extends HorizontalKineticBlock implements IBE<IotaReaderBlockEntity> {

    public IotaReaderBlock(Settings properties) {
        super(properties);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.get(HORIZONTAL_FACING).getAxis();
    }

    @Override
    public Class<IotaReaderBlockEntity> getBlockEntityClass() {
        return IotaReaderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends IotaReaderBlockEntity> getBlockEntityType() {
        return ModBlockEntities.IOTA_READER;
    }

    @Override
    public boolean hasShaftTowards(WorldView world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.get(HORIZONTAL_FACING).getAxis();
    }

    public static List<BlockPos> getCanSendIotaPackToPos(BlockState state, BlockPos pos) {
        var axis = state.get(HORIZONTAL_FACING).getAxis();
        if(axis == Direction.Axis.X) {
            return List.of(pos.add(0, 1, 0), pos.add(0, 0, 1), pos.add(0, 0, -1));
        } else if(axis == Direction.Axis.Z) {
            return List.of(pos.add(0, 1, 0), pos.add(1, 0, 0), pos.add(-1, 0, 0));
        }
        return List.of(pos.add(0, 1, 0));
    }
}
