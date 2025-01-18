package pub.pigeon.yggdyy.hexcreating.api.iotanet;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IIotaTerminalHost {
    List<IotaTerminal> getTerminals();
    boolean tryReceiveIotaPack(IotaPack pack, IotaTerminal from, IotaTerminal to);

    @Nullable
    IotaTerminal getDefaultTerminal(IotaTerminal from);
}
