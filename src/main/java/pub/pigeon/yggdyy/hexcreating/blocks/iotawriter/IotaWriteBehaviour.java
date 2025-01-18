package pub.pigeon.yggdyy.hexcreating.blocks.iotawriter;

import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.api.item.HexHolderItem;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import at.petrak.hexcasting.common.lib.HexSounds;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaNetProcessor;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaPack;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaTerminal;

import java.util.List;

public class IotaWriteBehaviour extends BeltProcessingBehaviour {
    public static final BehaviourType<IotaWriteBehaviour> TYPE = new BehaviourType<>();
    public IotaWriteBlockEntity blockEntity;
    public long needWriteTime = 0L, haveWriteTime = 0L;
    public static String NEED_WRITE_TIME_KEY = "need_write_time", HAVE_WRITE_TIME_KEY = "have_write_time";
    public Iota targetIota = new NullIota();
    public static String TARGET_IOTA_KEY = "write_target_iota";

    public IotaWriteBehaviour(IotaWriteBlockEntity be) {
        super(be);
        this.blockEntity = be;
        whenItemEnters(this::itemEnter);
        whileItemHeld(this::itemHeld);
    }

    private ProcessingResult itemHeld(TransportedItemStack transportedItemStack, TransportedItemStackHandlerBehaviour handler) {
        if(blockEntity.getWorld() == null) return ProcessingResult.PASS;
        haveWriteTime += Math.abs(blockEntity.getSpeed());

        if(haveWriteTime >= needWriteTime) {
            this.haveWriteTime = this.needWriteTime = 0L;

            if(transportedItemStack.stack.getItem() instanceof IotaHolderItem item) {
                item.writeDatum(transportedItemStack.stack, targetIota);
            } else if(transportedItemStack.stack.getItem() instanceof  HexHolderItem item) {
                if(targetIota.executable()) {
                    item.writeHex(
                            transportedItemStack.stack,
                            List.of(targetIota),
                            item.getPigment(transportedItemStack.stack),
                            item.getMedia(transportedItemStack.stack)
                    );
                } else if(targetIota instanceof ListIota listIota) {
                    List<Iota> iotaList = new java.util.ArrayList<>(List.of());
                    for (Iota iota : listIota.getList()) {
                        iotaList.add(iota);
                    }
                    item.writeHex(
                            transportedItemStack.stack,
                            iotaList,
                            item.getPigment(transportedItemStack.stack),
                            item.getMedia(transportedItemStack.stack)
                    );
                }
            }

            targetIota = new NullIota();
            blockEntity.sync();
            return ProcessingResult.PASS;
        }
        blockEntity.sync();
        return ProcessingResult.HOLD;
    }
    private ProcessingResult itemEnter(@NotNull TransportedItemStack transportedItemStack, TransportedItemStackHandlerBehaviour handler) {
        if(transportedItemStack.stack.getItem() instanceof IotaHolderItem item) {
            if(item.writeable(transportedItemStack.stack) && item.canWrite(transportedItemStack.stack, blockEntity.iota)) {
                this.targetIota = blockEntity.iota;
                this.haveWriteTime = 0L;
                this.needWriteTime = getIotaWriteNeedTimeArg() * 128L * this.targetIota.size() * transportedItemStack.stack.getCount();
                blockEntity.sync();
                return ProcessingResult.HOLD;
            } else return ProcessingResult.PASS;
        } else if(transportedItemStack.stack.getItem() instanceof HexHolderItem item) {
            if(blockEntity.iota.executable()) {
                this.targetIota = blockEntity.iota;
                this.haveWriteTime = 0L;
                this.needWriteTime = getIotaWriteNeedTimeArg() * 128L * this.targetIota.size() * transportedItemStack.stack.getCount();
                blockEntity.sync();
                return ProcessingResult.HOLD;
            } else if(blockEntity.iota instanceof ListIota listIota && isListIotaExecuteAble(listIota)) {
                this.targetIota = blockEntity.iota;
                this.haveWriteTime = 0L;
                this.needWriteTime = getIotaWriteNeedTimeArg() * 128L * this.targetIota.size() * transportedItemStack.stack.getCount();
                blockEntity.sync();
                return ProcessingResult.HOLD;
            }
        }
        return ProcessingResult.PASS;
    }
    public long getIotaWriteNeedTimeArg() {
        return 20L;
    }
    public boolean isListIotaExecuteAble(@NotNull ListIota iota) {
        if(iota.subIotas() == null) return false;
        for(var i : iota.subIotas()) {
            if(!i.executable()) return false;
        } return true;
    }

    @Override
    public void read(NbtCompound nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        this.haveWriteTime = nbt.contains(HAVE_WRITE_TIME_KEY)? nbt.getLong(HAVE_WRITE_TIME_KEY) : 0;
        this.needWriteTime = nbt.contains(NEED_WRITE_TIME_KEY)? nbt.getLong(NEED_WRITE_TIME_KEY) : 0;
        if(!clientPacket) {
            this.targetIota = nbt.contains(TARGET_IOTA_KEY)? IotaType.deserialize(nbt.getCompound(TARGET_IOTA_KEY), (ServerWorld) blockEntity.getWorld()) : new NullIota();
        }
    }

    @Override
    public void write(NbtCompound nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        nbt.putLong(HAVE_WRITE_TIME_KEY, this.haveWriteTime);
        nbt.putLong(NEED_WRITE_TIME_KEY, this.needWriteTime);
        if(!clientPacket) {
            nbt.put(TARGET_IOTA_KEY, IotaType.serialize(targetIota));
        }
    }
}
