package pub.pigeon.yggdyy.hexcreating.cast;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import pub.pigeon.yggdyy.hexcreating.cast.iota.TranslateIota;

public class ModIotaTypes {
    public static void init() {
        //HexIotaTypes.REGISTRY.createEntry(TRANSLATE);
    }
    public static final IotaType<TranslateIota> TRANSLATE = TranslateIota.TYPE;
}
