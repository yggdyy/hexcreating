package pub.pigeon.yggdyy.hexcreating.create.train;

import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.content.trains.track.AllPortalTracks;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.Pair;
import io.github.fabricators_of_create.porting_lib.entity.ITeleporter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;
import pub.pigeon.yggdyy.hexcreating.create.train.portal_tracks.TrainGateTeleporter;

import java.util.function.Function;

public class ModPortalTracks {
    public static void init() {
        AllPortalTracks.registerIntegration(ModBlocks.INSTANCE.getTRAIN_GATE(), p -> notStandardPortalProvider(p, world -> new TrainGateTeleporter()));
    }

    //in the same level, quite NOTSTANDARD, right?
    public static Pair<ServerWorld, BlockFace> notStandardPortalProvider(Pair<ServerWorld, BlockFace> inbound, Function<ServerWorld, ITeleporter> customPortalForcer) {
        ServerWorld level = inbound.getFirst();
        RegistryKey<World> resourceKey = level.getRegistryKey();
        MinecraftServer minecraftserver = level.getServer();
        ServerWorld otherLevel = minecraftserver.getWorld(resourceKey);

        if (otherLevel == null)
            return null;

        BlockFace inboundTrack = inbound.getSecond();
        BlockPos portalPos = inboundTrack.getConnectedPos();
        BlockState portalState = level.getBlockState(portalPos);
        ITeleporter teleporter = customPortalForcer.apply(otherLevel);

        SuperGlueEntity probe = new SuperGlueEntity(level, new Box(portalPos));
        probe.setYaw(inboundTrack.getFace()
                .asRotation());
        probe.setPortalEntrancePos();

        TeleportTarget portalInfo = teleporter.getPortalInfo(probe, otherLevel, probe::getTeleportTarget);
        if (portalInfo == null)
            return null;

        BlockPos otherPortalPos = BlockPos.ofFloored(portalInfo.position);
        BlockState otherPortalState = otherLevel.getBlockState(otherPortalPos);
        if (otherPortalState.getBlock() != portalState.getBlock())
            return null;

        Direction targetDirection = inboundTrack.getFace();
        if (targetDirection.getAxis() == otherPortalState.get(Properties.HORIZONTAL_AXIS))
            targetDirection = targetDirection.rotateYClockwise();
        BlockPos otherPos = otherPortalPos.offset(targetDirection);
        //otherLevel.getPlayers().get(0).sendMessage(Text.literal("" + otherPos));
        return Pair.of(otherLevel, new BlockFace(otherPos, targetDirection.getOpposite()));
    }
}
