package pub.pigeon.yggdyy.hexcreating.create;

import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.block.Block;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;

import java.util.Map;

public class ModStressValueProvider implements BlockStressValues.IStressValueProvider {
    public static Map<Block, Double> IMPACT = Map.of(ModBlocks.INSTANCE.getCIRCLE_AMPLIFIER(), 4.0);
    @Override
    public double getImpact(Block block) {
        return IMPACT.containsKey(block)? IMPACT.get(block) : 0;
    }

    @Override
    public double getCapacity(Block block) {
        return 0;
    }

    @Override
    public boolean hasImpact(Block block) {
        return IMPACT.containsKey(block);
    }

    @Override
    public boolean hasCapacity(Block block) {
        return false;
    }

    @Nullable
    @Override
    public Couple<Integer> getGeneratedRPM(Block block) {
        return null;
    }
}
