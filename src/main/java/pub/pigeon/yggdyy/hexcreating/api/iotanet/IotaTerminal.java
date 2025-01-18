package pub.pigeon.yggdyy.hexcreating.api.iotanet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IotaTerminal {
    public int terminalId;
    public Long lastAcceptTime = -1L;
    public List<Long> receivedId = new ArrayList<>(List.of());
    public BlockEntity blockEntity;

    public IotaTerminal(BlockEntity be, int terminalId) {
        this.blockEntity = be;
        this.terminalId = terminalId;
    }

    public void readNbt(NbtCompound nbt) {
        this.lastAcceptTime = nbt.contains(getBaseNbtKey() + "last_accept_time")?
                nbt.getLong(getBaseNbtKey() + "last_accept_time") : -1L;
        if(nbt.contains(getBaseNbtKey() + "received_id_length")) {
            for(int i = 0; i < receivedId.size(); ++i) {
                receivedId.add(nbt.getLong(getBaseNbtKey() + "received_id_" + i));
            }
        }
    }
    public void writeNbt(NbtCompound nbt) {
        nbt.putLong(getBaseNbtKey() + "last_accept_time", lastAcceptTime);
        nbt.putInt(getBaseNbtKey() + "received_id_length", receivedId.size());
        for(int i = 0; i < receivedId.size(); ++i) {
            nbt.putLong(getBaseNbtKey() + "received_id_" + i, receivedId.get(i));
        }
    }

    public String getBaseNbtKey() {
        return "iota_terminal_" + terminalId + "_";
    }

    public boolean canAcceptIotaPack(IotaPack p) {
        if(this.blockEntity.getWorld() == null) return false;
        if(this.blockEntity.getWorld().getTime() == this.lastAcceptTime)
            return !this.receivedId.contains(p.getId());
        else
            return true;
    }
    public void confirmAcceptIotaPack(IotaPack p) {
        if(this.lastAcceptTime != this.blockEntity.getWorld().getTime()) {
            this.lastAcceptTime = this.blockEntity.getWorld().getTime();
            this.receivedId = new ArrayList<>(List.of(p.getId()));
        } else this.receivedId.add(p.getId());
        blockEntity.markDirty();
    }
}
