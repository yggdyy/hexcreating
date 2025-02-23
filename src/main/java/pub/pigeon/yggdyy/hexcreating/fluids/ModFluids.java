package pub.pigeon.yggdyy.hexcreating.fluids;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;

public class ModFluids {
    public static FlowableFluid STILL_MEDIA = Registry.register(Registries.FLUID, new Identifier(HexcreatingMain.MOD_ID, "media"), new MediaFluid.Still());
    public static FlowableFluid FLOWING_MEDIA = Registry.register(Registries.FLUID, new Identifier(HexcreatingMain.MOD_ID, "flowing_media"), new MediaFluid.Flowing());
    public static Item MEDIA_BUCKET = Registry.register(Registries.ITEM, new Identifier(HexcreatingMain.MOD_ID, "media_bucket"), new BucketItem(STILL_MEDIA, new FabricItemSettings().maxCount(1).recipeRemainder(Items.BUCKET)));
    public static Block MEDIA = Registry.register(Registries.BLOCK, new Identifier(HexcreatingMain.MOD_ID, "media"), new FluidBlock(STILL_MEDIA, FabricBlockSettings.copyOf(Blocks.WATER)));

    public static void init() {
        registerFluidStorage();
    }

    private static void registerFluidStorage() {

    }
}
