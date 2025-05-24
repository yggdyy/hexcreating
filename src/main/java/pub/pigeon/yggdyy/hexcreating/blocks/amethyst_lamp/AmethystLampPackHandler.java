package pub.pigeon.yggdyy.hexcreating.blocks.amethyst_lamp;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;

public class AmethystLampPackHandler implements ServerPlayNetworking.PlayChannelHandler {
    public static final Identifier ID = new Identifier(HexcreatingMain.MOD_ID, "update_amethyst_lamp");

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        BlockPos target = buf.readBlockPos();
        int power = buf.readInt();
        server.execute(() -> {
            ServerWorld world = player.getServerWorld();
            if(world == null || !world.getBlockState(target).isOf(ModBlocks.INSTANCE.getAMETHYST_LAMP())) return;
            world.setBlockState(target, world.getBlockState(target).with(Properties.POWER, power), 3);
        });
    }
}
