package pub.pigeon.yggdyy.hexcreating.items;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;

public class ModItems {
    public static Item IOTA_WIRE_REEL = register("iota_wire_reel", new IotaWireReelItem(new Item.Settings()));
    public static Item EMPTY_REEL = register("empty_reel", new EmptyReelItem(new Item.Settings()));
    public static Item IOTA_WIRE_CONNECTOR_TECH = register("iota_wire_connector_tech", new Item(new Item.Settings()));
    public static Item IOTA_WIRE_CONNECTOR = register("iota_wire_connector", new IotaWireConnectorItem(new Item.Settings()));
    public static Item IOTA_WIRE = register("iota_wire", new Item(new Item.Settings()));

    public static void init() {}
    public static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, new Identifier(HexcreatingMain.MOD_ID, name), item);
    }
}
