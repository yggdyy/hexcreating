package pub.pigeon.yggdyy.hexcreating.blocks.circle_amplifier;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;

public class CircleAmplifierBlockEntity extends KineticBlockEntity {
    public CircleAmplifierBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CIRCLE_AMPLIFIER, pos, state);
    }

}
