package pub.pigeon.yggdyy.hexcreating.create.spout;

import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.common.recipe.BrainsweepRecipe;
import at.petrak.hexcasting.common.recipe.HexRecipeStuffRegistry;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.fluids.SoulFluid;

import java.util.List;
import java.util.stream.Collectors;

public class FlayMindBySpout {
    @Nullable
    public static List<BrainsweepRecipe> RECIPES = null;
    private static final Random RANDOM = Random.create();

    public static void checkRecipes(World world) {
        if(RECIPES == null) {
            RECIPES = world.getRecipeManager().values().stream()
                    .filter(recipe -> recipe.getType().equals(HexRecipeStuffRegistry.BRAINSWEEP_TYPE))
                    .map(recipe -> (BrainsweepRecipe)recipe)
                    .collect(Collectors.toList());
        }
    }
    public static List<BrainsweepRecipe> getSuitableRecipes(World world, Block block, Identifier entityId) {
        return RECIPES.stream().filter(brainsweepRecipe -> {
            boolean checkBlock = brainsweepRecipe.blockIn().test(block.getDefaultState());
            boolean checkEntity = EntityType.getId(brainsweepRecipe.entityIn().exampleEntity(world).getType()).equals(entityId);
            boolean checkOutput = !brainsweepRecipe.result().getBlock().asItem().equals(Items.AIR);
            return checkBlock && checkEntity && checkOutput;
        }).collect(Collectors.toList());
    }

    public static boolean isItemFlayMindAble(World world, ItemStack stack) {
        checkRecipes(world);
        if(stack.getItem() instanceof BlockItem item) {
            var block = item.getBlock();
            for(var r : RECIPES) {
                if(r.blockIn().test(block.getDefaultState()))
                    return true;
            }
        }
        return false;
    }

    public static long getItemRequiredSoul(World world, ItemStack stack, FluidStack providedFluid) {
        checkRecipes(world);
        if(isItemFlayMindAble(world, stack) && providedFluid.getFluid() instanceof SoulFluid && providedFluid.hasTag() && providedFluid.getTag().contains(SoulFluid.ID_KEY)) {
            var entityId = Identifier.tryParse(providedFluid.getTag().getString(SoulFluid.ID_KEY));
            if(entityId == null) return -1;
            var suit = getSuitableRecipes(world, ((BlockItem)stack.getItem()).getBlock(), entityId);
            if(suit == null || suit.size() < 1) return -1;
            long ret = 0L;
            for(var r : suit)
                ret += r.mediaCost();
            ret = ret / suit.size() / MediaConstants.DUST_UNIT * 81L;
            return ret <= providedFluid.getAmount()? ret : -1;
        }
        return -1;
    }

    @Nullable
    public static ItemStack flayMindItem(World world, long requiredAmount, ItemStack stack, FluidStack providedFluid) {
        checkRecipes(world);
        if(isItemFlayMindAble(world, stack) && providedFluid.getFluid() instanceof SoulFluid && providedFluid.hasTag() && providedFluid.getTag().contains(SoulFluid.ID_KEY)) {
            var entityId = Identifier.tryParse(providedFluid.getTag().getString(SoulFluid.ID_KEY));
            if(entityId == null) return null;
            var suit = getSuitableRecipes(world, ((BlockItem)stack.getItem()).getBlock(), entityId);
            if(suit == null || suit.size() < 1) return null;
            long ret = 0L;
            for(var r : suit)
                ret += r.mediaCost();
            ret = ret / suit.size() / MediaConstants.DUST_UNIT * 81L;
            if(ret != requiredAmount) return null;
            int cur = RANDOM.nextBetween(0, suit.size() - 1);
            providedFluid.shrink(requiredAmount);
            stack.split(1);
            return suit.get(cur).result().getBlock().asItem().getDefaultStack();
        }
        return null;
    }

}
