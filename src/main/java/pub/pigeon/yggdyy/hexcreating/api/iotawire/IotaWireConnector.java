package pub.pigeon.yggdyy.hexcreating.api.iotawire;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IIotaTerminalHost;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaNetProcessor;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaPack;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaTerminal;
import pub.pigeon.yggdyy.hexcreating.items.ModItems;

import java.util.ArrayList;
import java.util.List;

public class IotaWireConnector {
    @NotNull
    public IotaTerminal terminal;
    //public List<IotaWireConnector> others;
    public Box clickBox;
    public List<Pair<BlockPos, Integer>> others;

    public IotaWireConnector(@NotNull IotaTerminal terminal, Box box) {
        this.terminal = terminal;
        this.clickBox = box;
        //this.others = List.of();
        this.others = new ArrayList<>(List.of());
    }

    public String getBaseNbtKey() {
        return "iota_connector_" + terminal.terminalId + "_";
    }

    //
    public void writeNbt(NbtCompound nbt) {
        terminal.writeNbt(nbt);
        nbt.putInt(getBaseNbtKey() + "others_length", others.size());
        for(int i = 0; i < others.size(); ++i) {
            String posKey = getBaseNbtKey() + "others_" + i + "_pos_";
            nbt.putInt(posKey + "x", others.get(i).getLeft().getX());
            nbt.putInt(posKey + "y", others.get(i).getLeft().getY());
            nbt.putInt(posKey + "z", others.get(i).getLeft().getZ());

            String idKey = getBaseNbtKey() + "others_" + i + "_id";
            nbt.putInt(idKey, others.get(i).getRight());
        }


    }

    public void readNbt(NbtCompound nbt) {
        terminal.readNbt(nbt);
        int s = nbt.contains(getBaseNbtKey() + "others_length")? nbt.getInt(getBaseNbtKey() + "others_length") : 0;
        for(int i = 0; i < s; ++i) {
            String posKey = getBaseNbtKey() + "others_" + i + "_pos_";
            String idKey = getBaseNbtKey() + "others_" + i + "_id";
            BlockPos newPos = new BlockPos(
                    nbt.getInt(posKey + "x"),
                    nbt.getInt(posKey + "y"),
                    nbt.getInt(posKey + "z")
            );
            int newId = nbt.getInt(idKey);
            others.add(new Pair<>(newPos, newId));
        }
    }

    //only on server
    public void disConnectAll() {
        ServerWorld world = (ServerWorld) terminal.blockEntity.getWorld();
        List<Pair<BlockPos, Integer>> tmp = List.copyOf(others);
        for(var o : tmp) {
            BlockEntity e = world.getBlockEntity(o.getLeft()); //just to load the chunk
            IotaWireProcessor.tryDisConnect2Connectors(world, terminal.blockEntity.getPos(), terminal.terminalId, o.getLeft(), o.getRight());
        }
        var stack = new ItemStack(ModItems.IOTA_WIRE);
        stack.setCount(tmp.size());
        world.spawnEntity(new ItemEntity(world, terminal.blockEntity.getPos().toCenterPos().getX(), terminal.blockEntity.getPos().toCenterPos().getY(), terminal.blockEntity.getPos().toCenterPos().getZ(),stack));
        this.terminal.blockEntity.markDirty();
    }

    public void sendIotaPackToAll(IotaPack pack) {
        for(int i = 0; i < others.size(); ++i) {
            IotaNetProcessor.trySendIotaPack(pack.copyWithSameId(), terminal, (IIotaTerminalHost) terminal.blockEntity.getWorld().getBlockEntity(others.get(i).getLeft()), IotaWireProcessor.tryGetConnector((ServerWorld) terminal.blockEntity.getWorld(), others.get(i).getLeft(), others.get(i).getRight()).terminal);
        }
    }
}
