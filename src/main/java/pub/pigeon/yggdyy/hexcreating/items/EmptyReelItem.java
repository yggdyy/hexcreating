package pub.pigeon.yggdyy.hexcreating.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import pub.pigeon.yggdyy.hexcreating.api.iotawire.IIotaWireConnectorHost;
import pub.pigeon.yggdyy.hexcreating.api.iotawire.IotaWireProcessor;

public class EmptyReelItem extends Item {

    public EmptyReelItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext itemUsageContext) {
        if(!itemUsageContext.getWorld().isClient && !itemUsageContext.getPlayer().isSneaking()) {
            //itemUsageContext.getPlayer().sendMessage(Text.literal("useOnBlock"));
            if(itemUsageContext.getWorld().getBlockEntity(itemUsageContext.getBlockPos()) instanceof IIotaWireConnectorHost be) {
                var targetRPos = itemUsageContext.getHitPos().add(-itemUsageContext.getBlockPos().getX(), -itemUsageContext.getBlockPos().getY(), -itemUsageContext.getBlockPos().getZ());
                double eps = 0.005;
                var targetBox = VoxelShapes.cuboid(targetRPos.x - eps, targetRPos.y - eps, targetRPos.z - eps, targetRPos.x + eps, targetRPos.y + eps, targetRPos.z + eps);
                var cList = be.getIotaWireConnectors();

                //itemUsageContext.getPlayer().sendMessage(Text.literal("ygg!"));

                for(var c : cList) {
                    if(c.clickBox.intersects(targetBox.getBoundingBox())) {
                        var stack = itemUsageContext.getStack();
                        var pos = itemUsageContext.getBlockPos();
                        var id = c.terminal.terminalId;

                        var preNbt = stack.getOrCreateNbt();
                        if(!preNbt.contains("cur") || preNbt.getInt("cur") == 0) {
                            preNbt.putInt("pos0_x", pos.getX());
                            preNbt.putInt("pos0_y", pos.getY());
                            preNbt.putInt("pos0_z", pos.getZ());
                            preNbt.putInt("id0", id);
                            preNbt.putInt("cur", 1);

                            itemUsageContext.getPlayer().sendMessage(Text.literal("(" + pos.getX() + "," + pos.getY() + "," + pos.getZ() + ")"));
                        } else {
                            preNbt.putInt("pos1_x", pos.getX());
                            preNbt.putInt("pos1_y", pos.getY());
                            preNbt.putInt("pos1_z", pos.getZ());
                            preNbt.putInt("id1", id);
                            preNbt.putInt("cur", 0);

                            itemUsageContext.getPlayer().sendMessage(Text.literal("(" + pos.getX() + "," + pos.getY() + "," + pos.getZ() + ")"));
                        }
                        stack.setNbt(preNbt);

                        return ActionResult.SUCCESS;
                    }
                }
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if(!world.isClient && playerEntity.isSneaking()) {
            var stack = playerEntity.getStackInHand(hand);
            if(stack.getNbt() != null && stack.getNbt().contains("id0") && stack.getNbt().contains("id1")) {
                var nbt = stack.getNbt();
                BlockPos pos0 = new BlockPos(nbt.getInt("pos0_x"), nbt.getInt("pos0_y"), nbt.getInt("pos0_z")),
                        pos1 = new BlockPos(nbt.getInt("pos1_x"), nbt.getInt("pos1_y"), nbt.getInt("pos1_z"));
                int id0 = nbt.getInt("id0"), id1 = nbt.getInt("id1");

                //playerEntity.sendMessage(Text.literal("ygg!"));

                if(pos0.equals(pos1) && id0 == id1) {
                    playerEntity.sendMessage(Text.translatable("hexcreating.iota_wire_reel.same"));
                } else if(pos0.toCenterPos().distanceTo(pos1.toCenterPos()) > getMaxDis()){
                    playerEntity.sendMessage(Text.translatable("hexcreating.iota_wire_reel.far"));
                } else {
                    if(IotaWireProcessor.tryDisConnect2Connectors((ServerWorld) world, pos0, id0, pos1, id1)) {
                        if(!playerEntity.isCreative()) {
                            stack.setCount(stack.getCount() - 1);
                            playerEntity.giveItemStack(new ItemStack(ModItems.IOTA_WIRE_REEL));
                        }
                        playerEntity.sendMessage(Text.translatable("hexcreating.iota_wire_reel.success"));
                        return TypedActionResult.consume(stack);
                    } else {
                        playerEntity.sendMessage(Text.translatable("hexcreating.iota_wire_reel.fail"));
                    }
                }
            } else {
                playerEntity.sendMessage(Text.translatable("hexcreating.iota_wire_reel.unfinished"));
            }
        }
        return TypedActionResult.pass(playerEntity.getStackInHand(hand));
    }

    public static double getMaxDis() {
        return 16;
    }
}
