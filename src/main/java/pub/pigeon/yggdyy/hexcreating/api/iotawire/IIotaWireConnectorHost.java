package pub.pigeon.yggdyy.hexcreating.api.iotawire;

import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IIotaTerminalHost;

import java.util.List;

public interface IIotaWireConnectorHost extends IIotaTerminalHost {
    List<IotaWireConnector> getIotaWireConnectors();
    void sync();
}
