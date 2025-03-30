package pub.pigeon.yggdyy.hexcreating.create.train.portal_tracks;

import io.github.fabricators_of_create.porting_lib.entity.ITeleporter;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;
import pub.pigeon.yggdyy.hexcreating.blocks.train_gate.TrainGateBlock;
import pub.pigeon.yggdyy.hexcreating.blocks.train_gate.TrainGateCoreBlockEntity;

import java.util.function.Function;

public class TrainGateTeleporter implements ITeleporter{
    @Override
    public @Nullable TeleportTarget getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, TeleportTarget> defaultPortalInfo) {
        //return new TeleportTarget(entity.getPos().add(new Vec3d(10, 0, 0)), entity.getVelocity(), entity.getYaw(), entity.getPitch());
        BlockPos pos = entity.getBlockPos();
        World world = entity.getWorld();
        if(world != null && !world.isClient && world.getBlockState(pos).isOf(ModBlocks.INSTANCE.getTRAIN_GATE())) {
            BlockPos corePos = ((TrainGateBlock)ModBlocks.INSTANCE.getTRAIN_GATE()).getCorePos(world, pos);
            if(corePos != null) {
                TrainGateCoreBlockEntity beFrom = (TrainGateCoreBlockEntity) world.getBlockEntity(corePos);
                BlockPos otherCorePos = beFrom.toPos;
                TrainGateCoreBlockEntity beTo = (TrainGateCoreBlockEntity) world.getBlockEntity(otherCorePos);
                if(beTo != null) {
                    BlockPos idealTarget = pos.add(corePos.multiply(-1)).add(otherCorePos);
                    if(!beTo.isInGate(idealTarget)) idealTarget = otherCorePos.add(0, 1, 0);
                    return new TeleportTarget(idealTarget.toCenterPos(), entity.getVelocity(), entity.getYaw(), entity.getPitch());
                }
            }
        }
        return null;
    }
}
