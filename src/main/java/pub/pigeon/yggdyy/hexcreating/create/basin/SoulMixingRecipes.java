package pub.pigeon.yggdyy.hexcreating.create.basin;

import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;
import pub.pigeon.yggdyy.hexcreating.fluids.ModFluids;
import pub.pigeon.yggdyy.hexcreating.fluids.SoulFluid;
import pub.pigeon.yggdyy.hexcreating.items.ModItems;
import pub.pigeon.yggdyy.hexcreating.libs.NbtSensitiveIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoulMixingRecipes{
    public static void generateSoulMixingRecipes(MinecraftServer server) {
        var manager = server.getRecipeManager();
        List<Recipe<?>> extendedRecipes = new ArrayList<>(manager.values());
        Registries.ENTITY_TYPE.stream().filter(entityType -> {
            return true;
        }).forEach(entityType -> {
            Identifier entityId = EntityType.getId(entityType);
            var iStackIn = new ItemStack(ModItems.RAW_SOUL, 1);
            ModItems.RAW_SOUL.setEntityId(iStackIn, entityId);
            ModItems.RAW_SOUL.setSoulAmount(iStackIn, 25);
            var fStackOut = new FluidStack(ModFluids.STILL_SOUL, 25 * 81L);
            fStackOut.getOrCreateTag().putString(SoulFluid.ID_KEY, entityId.toString());
            MixingRecipe recipe = new ProcessingRecipeBuilder<>(MixingRecipe::new,
                    new Identifier(HexcreatingMain.MOD_ID, "mixing/raw_soul_to_soul/" + entityId.getNamespace() + "/" + entityId.getPath())).require(new NbtSensitiveIngredient(Arrays.stream(Ingredient.ofStacks(iStackIn).entries)))
                    .require(FluidIngredient.fromFluid(ModFluids.STILL_MEDIA, 25 * 81L))
                    .output(new FluidStack(fStackOut.getFluid(), fStackOut.getAmount(), fStackOut.getTag()))
                    .requiresHeat(HeatCondition.HEATED)
                    .build();
            extendedRecipes.add(recipe);
        });
        manager.setRecipes(extendedRecipes);
    }
}
