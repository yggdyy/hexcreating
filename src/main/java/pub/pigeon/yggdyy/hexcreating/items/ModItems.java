package pub.pigeon.yggdyy.hexcreating.items;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;
import pub.pigeon.yggdyy.hexcreating.fluids.ModFluids;

public class ModItems {
    public static Item IOTA_WIRE_REEL = register("iota_wire_reel", new IotaWireReelItem(new Item.Settings()));
    public static Item EMPTY_REEL = register("empty_reel", new EmptyReelItem(new Item.Settings()));
    public static Item IOTA_WIRE_CONNECTOR_TECH = register("iota_wire_connector_tech", new Item(new Item.Settings()));
    public static Item IOTA_WIRE_CONNECTOR = register("iota_wire_connector", new IotaWireConnectorItem(new Item.Settings()));
    public static Item IOTA_WIRE = register("iota_wire", new Item(new Item.Settings()));
    public static RawSoulItem RAW_SOUL = register("raw_soul", new RawSoulItem(new Item.Settings().fireproof()));
    public static SequencedAssemblyItem UNFINISHED_TRAIN_GATE_FRAME = register("unfinished_train_gate_frame", new SequencedAssemblyItem(new Item.Settings().maxCount(1)));
    public static SquareItem SQUARE = register("square", new SquareItem(new Item.Settings()));
    public static PrintedPaperItem PRINTED_PAPER = register("printed_paper", new PrintedPaperItem(new Item.Settings()));
    public static PaperReelItem PAPER_REEL = register("paper_reel", new PaperReelItem(new Item.Settings().maxCount(1).maxDamage(1024)));

    public static void init() {
        CREATE_REGISTRATE.register();
        Registry.register(Registries.ITEM_GROUP, new Identifier(HexcreatingMain.MOD_ID, "default"), HEXCREATING_GROUP);
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
    public static final ItemEntry<Item> THOUGHT_KEY_PATTERN_INTRODUCTION = CREATE_REGISTRATE.item("thought_key/pattern_introduction", Item::new).register();
    public static final ItemEntry<Item> THOUGHT_KEY_PATTERN_LOCATE = CREATE_REGISTRATE.item("thought_key/pattern_locate", Item::new).register();
    public static final ItemEntry<Item> THOUGHT_KEY_PATTERN_CONSTANTS = CREATE_REGISTRATE.item("thought_key/pattern_constants", Item::new).register();
    public static final ItemEntry<Item> THOUGHT_KEY_PATTERN_CALCULATE = CREATE_REGISTRATE.item("thought_key/pattern_calculate", Item::new).register();
    public static final ItemEntry<Item> THOUGHT_KEY_PATTERN_SPELL_1 = CREATE_REGISTRATE.item("thought_key/pattern_spell_1", Item::new).register();

    public static final ItemGroup HEXCREATING_GROUP = FabricItemGroup.builder()
            .icon(() -> ModFluids.MEDIA_BUCKET.getDefaultStack())
            .displayName(Text.translatable("category.hexcreating.default"))
            .entries((displayContext, entries) -> {
                entries.add(ModFluids.MEDIA_BUCKET);
                entries.add(ModBlocks.INSTANCE.getBOARD());
                entries.add(SQUARE);
                entries.add(EMPTY_REEL);
                entries.add(PAPER_REEL);
                entries.add(ModBlocks.INSTANCE.getAMETHYST_LAMP());
                entries.add(ModBlocks.INSTANCE.getCIRCLE_AMPLIFIER());
                entries.add(ModBlocks.INSTANCE.getTRAIN_GATE_CORE());
                entries.add(ModBlocks.INSTANCE.getTRAIN_GATE_FRAME());
            })
            .build();
}
