package pub.pigeon.yggdyy.hexcreating.api.iotanet;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class IotaNetProcessor {

    //NOTICE! If you have to send the IotaPack to another terminal in tryReceiveIotaPack, you MUST call to.confirmAcceptIotaPack by yourself before doing so!!!!
    public static boolean trySendIotaPack(IotaPack pack, IotaTerminal from, IIotaTerminalHost toBlock, @NotNull IotaTerminal to) {
        if(to.canAcceptIotaPack(pack) && toBlock.tryReceiveIotaPack(pack, from, to)) {
            to.confirmAcceptIotaPack(pack);
            return true;
        }
        return false;
    }

    public static boolean trySendIotaPack(IotaPack pack, IotaTerminal from, BlockPos toPos, @NotNull ServerWorld world) {
        if(world.getBlockEntity(toPos) != null && world.getBlockEntity(toPos) instanceof IIotaTerminalHost toBlock) {
            var to = toBlock.getDefaultTerminal(from);
            if(to == null) return false;
            else return trySendIotaPack(pack, from, toBlock, to);
        } else return false;
    }

}
