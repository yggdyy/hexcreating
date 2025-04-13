package pub.pigeon.yggdyy.hexcreating.items;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
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
    public static RawSoulItem RAW_SOUL = register("raw_soul", new RawSoulItem(new Item.Settings().fireproof()));
    public static SequencedAssemblyItem UNFINISHED_TRAIN_GATE_FRAME = register("unfinished_train_gate_frame", new SequencedAssemblyItem(new Item.Settings().maxCount(1)));

    public static void init() {
        CREATE_REGISTRATE.register();
    }
    public static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, new Identifier(HexcreatingMain.MOD_ID, name), item);
    }

    //following are items registered through create
    public static final CreateRegistrate CREATE_REGISTRATE = CreateRegistrate.create(HexcreatingMain.MOD_ID);
    public static final ItemEntry<Item> THOUGHT_KEY_HEXCASTING_INTRODUCTION = CREATE_REGISTRATE.item("thought_key/hexcasting_introduction", Item::new).register();
    public static final ItemEntry<Item> THOUGHT_KEY_BASIC_IOTA = CREATE_REGISTRATE.item("thought_key/basic_iota", Item::new).register();
    public static final ItemEntry<Item> THOUGHT_KEY_STACK = CREATE_REGISTRATE.item("thought_key/stack", Item::new).register();
    public static final ItemEntry<Item> THOUGHT_KEY_VECTOR = CREATE_REGISTRATE.item("thought_key/vector", Item::new).register();
}
