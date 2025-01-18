package pub.pigeon.yggdyy.hexcreating.blocks.iotareader;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;

import java.util.List;

public class IotaReaderBlockEntity extends KineticBlockEntity {
    public IotaReaderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IOTA_READER, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(new IotaReadBehaviour(this));
    }

    @Override
    public float calculateStressApplied() {
        return 2f;
    }

    public List<BlockPos> getCanSendIotaPackToPos() {
        return IotaReaderBlock.getCanSendIotaPackToPos(world.getBlockState(pos), pos);
    }

    public void sync() {
        this.markDirty();
        if (world != null) {
            world.updateListeners(getPos(), world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }
}
