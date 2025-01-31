package pub.pigeon.yggdyy.hexcreating.blocks.iotapackobserver;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IIotaTerminalHost;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaPack;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaTerminal;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;

import java.util.List;

public class IotaPackObserverBlockEntity extends SmartBlockEntity implements IIotaTerminalHost {
    public IotaTerminal terminal = new IotaTerminal(this, 0);
    public int remainChargedTime = 0;
    public IotaPackObserverBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IOTA_PACK_OBSERVER, pos, state);
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public List<IotaTerminal> getTerminals() {
        return List.of(terminal);
    }

    @Override
    public boolean tryReceiveIotaPack(IotaPack pack, IotaTerminal from, IotaTerminal to) {
        if(world == null) return false;
        var iD = world.getBlockState(pos).get(IotaPackObserverBlock.FACING).getVector();
        var targetPos = pos.add(iD);
        if(from.blockEntity.getPos().equals(targetPos)) {
            if(remainChargedTime <= 0) {
                var preState = world.getBlockState(pos);
                world.setBlockState(pos, preState.with(IotaPackObserverBlock.CHARGED, true), 3);
            }
            this.remainChargedTime = defaultChargeTime();
            markDirty();
            return true;
        }
        return false;
    }

    @Override
    public @Nullable IotaTerminal getDefaultTerminal(IotaTerminal from) {
        return this.terminal;
    }

    @Override
    public void tick() {
        super.tick();
        if(world != null && !world.isClient) {
            if (remainChargedTime > 0) {
                --remainChargedTime;
                markDirty();
            }
            if (remainChargedTime <= 0) {
                var preState = world.getBlockState(pos);
                world.setBlockState(pos, preState.with(IotaPackObserverBlock.CHARGED, false), 3);
            }
        }
    }

    public int defaultChargeTime() {
        return 3;
    }
}
