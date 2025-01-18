package pub.pigeon.yggdyy.hexcreating.items;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;
import pub.pigeon.yggdyy.hexcreating.blocks.connector.IotaWireConnectorBlock;
import pub.pigeon.yggdyy.hexcreating.libs.IotaWireConnectorPropertyHelper;

public class IotaWireConnectorItem extends Item {
    public IotaWireConnectorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext itemUsageContext) {
        if(!itemUsageContext.getWorld().isClient) {
            ServerWorld world = (ServerWorld) itemUsageContext.getWorld();
            var dir = itemUsageContext.getSide().getOpposite();
            BlockPos targetPos = itemUsageContext.getBlockPos().add(dir.getOpposite().getVector());
            var state = world.getBlockState(targetPos);
            var pro = IotaWireConnectorPropertyHelper.getPropertyByDir(dir);
            if(state.isAir()) {
                var targetState = ModBlocks.INSTANCE.getIOTA_WIRE_CONNECTOR().getDefaultState();
                for(var p : IotaWireConnectorPropertyHelper.getAllProperty()) {
                    targetState = targetState.with(p, false);
                }
                targetState = targetState.with(pro, true);
                world.setBlockState(targetPos, targetState, 3);
                itemUsageContext.getStack().setCount(itemUsageContext.getStack().getCount() - 1);
                world.syncWorldEvent(3, targetPos, 3);
                return ActionResult.CONSUME;
            }
            if(state.isOf(ModBlocks.INSTANCE.getIOTA_WIRE_CONNECTOR())) {
                if(!state.get(pro)) {
                    var targetState = state.with(pro, true);
                    world.setBlockState(targetPos, targetState, 3);
                    itemUsageContext.getStack().setCount(itemUsageContext.getStack().getCount() - 1);
                    world.syncWorldEvent(3, targetPos, 3);
                    return ActionResult.CONSUME;
                }
                return ActionResult.PASS;
            }
        }
        return super.useOnBlock(itemUsageContext);
    }
}
