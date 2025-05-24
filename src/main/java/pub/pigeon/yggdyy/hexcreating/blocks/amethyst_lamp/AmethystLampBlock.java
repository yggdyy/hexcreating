package pub.pigeon.yggdyy.hexcreating.blocks.amethyst_lamp;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class AmethystLampBlock extends Block {
    public AmethystLampBlock(Settings settings) {
        super(settings.luminance(state -> state.get(Properties.POWER)));
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.POWER);
    }
    @Override
    public boolean emitsRedstonePower(BlockState blockState) {
        return true;
    }
    @Override
    public int getStrongRedstonePower(BlockState blockState, BlockView blockView, BlockPos blockPos, Direction direction) {
        return 0;
    }
    @Override
    public int getWeakRedstonePower(BlockState blockState, BlockView blockView, BlockPos blockPos, Direction direction) {
        return blockState.get(Properties.POWER);
    }
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
        return super.getPlacementState(itemPlacementContext).with(Properties.POWER, 0);
    }
}
