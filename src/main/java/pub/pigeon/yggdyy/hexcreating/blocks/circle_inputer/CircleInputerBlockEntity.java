package pub.pigeon.yggdyy.hexcreating.blocks.circle_inputer;

import at.petrak.hexcasting.api.block.HexBlockEntity;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IIotaTerminalHost;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaPack;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaTerminal;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;

import java.util.ArrayList;
import java.util.List;

public class CircleInputerBlockEntity extends HexBlockEntity implements IIotaTerminalHost {
    public List<Iota> iotas = List.of();
    public IotaTerminal terminal = new IotaTerminal(this, 0);

    public CircleInputerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CIRCLE_INPUTER, blockPos, blockState);
    }

    private static final String IOTAS_KEY = "hexcreating:iotas_";
    @Override
    protected void saveModData(NbtCompound tag) {
        if (world != null && !world.isClient) {
            terminal.writeNbt(tag);
            int l = iotas.size();
            tag.putInt(IOTAS_KEY + "length", l);
            for (int i = 0; i < l; ++i) {
                tag.put(IOTAS_KEY + i, IotaType.serialize(iotas.get(i)));
            }
        }
    }
    @Override
    protected void loadModData(NbtCompound tag) {
        if(world != null && !world.isClient) {
            terminal.readNbt(tag);
            List<Iota> list = new ArrayList<>(List.of());
            int l = tag.contains(IOTAS_KEY + "length") ? tag.getInt(IOTAS_KEY + "length") : 0;
            for (int i = 0; i < l; ++i) {
                list.add(IotaType.deserialize(tag.getCompound(IOTAS_KEY + i), (ServerWorld) world));
            }
            iotas = list;
        }
    }

    @Override
    public List<IotaTerminal> getTerminals() {
        return List.of(terminal);
    }

    @Override
    public boolean tryReceiveIotaPack(IotaPack pack, IotaTerminal from, IotaTerminal to) {
        var inT = world.getBlockState(pos).get(CircleInputerBlock.INPUT_TYPE);
        boolean ret = false;
        switch (inT) {
            case REJECT -> {
                ret = false;
            }
            case ALWAYS_SINGLE -> {
                this.iotas = List.of(pack.getIota());
                markDirty();
                ret = true;
            }
            case TRY_MULTIPLE -> {
                var rawIota = pack.getIota();
                if(Iota.typesMatch(rawIota, new ListIota(List.of()))) {
                    var listIota = (ListIota) rawIota;
                    List<Iota> preIotas = new ArrayList<>(List.of());
                    if(listIota.getList().size() > 0) listIota.subIotas().forEach(preIotas::add);
                    iotas = preIotas;
                } else {
                    iotas = List.of(rawIota);
                }
                markDirty();
                ret = true;
            }
        }
        return ret;
    }

    @Override
    public @Nullable IotaTerminal getDefaultTerminal(IotaTerminal from) {
        return terminal;
    }
}
