package pub.pigeon.yggdyy.hexcreating.blocks.iotapackobserver;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;

public class IotaPackObserverBlock extends WrenchableDirectionalBlock implements IBE<IotaPackObserverBlockEntity>{
    public static BooleanProperty CHARGED = BooleanProperty.of("charged");

    public IotaPackObserverBlock(Settings settings) {
        super(settings);
    }

    @Override
    public Class<IotaPackObserverBlockEntity> getBlockEntityClass() {
        return IotaPackObserverBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends IotaPackObserverBlockEntity> getBlockEntityType() {
        return ModBlockEntities.IOTA_PACK_OBSERVER;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHARGED);
        super.appendProperties(builder);
    }

    @Override
    public int getStrongRedstonePower(BlockState blockState, BlockView blockView, BlockPos blockPos, Direction direction) {
        return (boolean)blockState.get(CHARGED) && blockState.get(FACING) == direction ? 15 : 0;
    }

    @Override
    public int getWeakRedstonePower(BlockState blockState, BlockView blockView, BlockPos blockPos, Direction direction) {
        return (boolean)blockState.get(CHARGED) && blockState.get(FACING).getOpposite() != direction ? 15 : 0;
    }

    @Override
    public boolean emitsRedstonePower(BlockState blockState) {
        return true;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return super.getPlacementState(context).with(CHARGED, false);
    }
}
