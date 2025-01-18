package pub.pigeon.yggdyy.hexcreating.blocks.iotawriter;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.HexcreatingClient;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IIotaTerminalHost;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaPack;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaTerminal;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;
import pub.pigeon.yggdyy.hexcreating.blocks.iotareader.IotaReadBehaviour;

import java.util.List;
import java.util.Map;

public class IotaWriteBlockEntity extends KineticBlockEntity implements IIotaTerminalHost {
    public IotaTerminal localTerminal;
    public Iota iota;
    public static String IOTA_KEY = "local_iota";
    public IotaWriteBehaviour iotaWriteBehaviour;

    public IotaWriteBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IOTA_WRITER, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.iotaWriteBehaviour = new IotaWriteBehaviour(this);
        behaviours.add(iotaWriteBehaviour);
        this.localTerminal = new IotaTerminal(this, 0);
        this.iota = new NullIota();
    }

    @Override
    public float calculateStressApplied() {
        return 2f;
    }

    @Override
    public List<IotaTerminal> getTerminals() {
        return List.of(localTerminal);
    }

    @Override
    public boolean tryReceiveIotaPack(IotaPack pack, IotaTerminal from, IotaTerminal to) {
        this.iota = pack.getIota();
        this.markDirty();
        return true;
    }

    @Override
    public @Nullable IotaTerminal getDefaultTerminal(IotaTerminal from) {
        return localTerminal;
    }

    public void sync() {
        this.markDirty();
        if (world != null) {
            world.updateListeners(getPos(), world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }

    @Override
    protected void write(NbtCompound compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if(!clientPacket) {
            localTerminal.writeNbt(compound);
            compound.put(IOTA_KEY, IotaType.serialize(iota));
        }
    }

    @Override
    protected void read(NbtCompound compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if(!clientPacket) {
            localTerminal.readNbt(compound);
            iota = IotaType.deserialize(compound.getCompound(IOTA_KEY), (ServerWorld) getWorld());
        }
    }

    public float getDecorationRotationOmega() {
        float ret = Math.abs(getSpeed());
        //ret = Math.min(ret, 64f * 3.1416f / 600f);
        ret = (float) (Math.log(ret) / Math.log(2)) * 3.1416f / 200f;
        return this.getSpeed() > 0 ? ret : -ret;
    }
}
