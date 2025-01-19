package pub.pigeon.yggdyy.hexcreating.api.iotawire;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class IotaWireProcessor {
    @Nullable
    public static IotaWireConnector tryGetConnector(World world, BlockPos targetPos, int id) {
        if(world.getBlockEntity(targetPos) != null && world.getBlockEntity(targetPos) instanceof IIotaWireConnectorHost be) {
            var connectors = be.getIotaWireConnectors();
            for(var c : connectors) {
                if(c.terminal.terminalId == id) return c;
            }
        }
        return null;
    }

    public static boolean tryConnect2Connectors(ServerWorld world, BlockPos pos1, int id1, BlockPos pos2, int id2) {
        if(pos1.equals(pos2) && id1 == id2) return false;
        IotaWireConnector c1 = tryGetConnector(world, pos1, id1), c2 = tryGetConnector(world, pos2, id2);
        if(c1 != null && c2 != null) {
            if(isConnected(c1, c2)) return false;
            c1.others.add(new Pair<>(pos2, id2));
            c2.others.add(new Pair<>(pos1, id1));
            c1.terminal.blockEntity.markDirty();
            c2.terminal.blockEntity.markDirty();
            ((IIotaWireConnectorHost) c1.terminal.blockEntity).sync();
            ((IIotaWireConnectorHost) c2.terminal.blockEntity).sync();
            return true;
        }
        return false;
    }

    //only use this when you are sure the 2 connectors REALLY exist!
    public static boolean isConnected(IotaWireConnector c1, IotaWireConnector c2) {
        for(int i = 0; i < c1.others.size(); ++i) {
            if(c1.others.get(i).getLeft().equals(c2.terminal.blockEntity.getPos()) && c1.others.get(i).getRight() == c2.terminal.terminalId) {
                return true;
            }
        }
        return false;
    }

    public static boolean tryDisConnect2Connectors(ServerWorld world, BlockPos pos1, int id1, BlockPos pos2, int id2) {
        IotaWireConnector c1 = tryGetConnector(world, pos1, id1), c2 = tryGetConnector(world, pos2, id2);
        if(c1 != null && c2 !=null) {
            if(isConnected(c1, c2)) {
                for(int i = 0; i < c1.others.size(); ++i) {
                    if(c1.others.get(i).getLeft().equals(c2.terminal.blockEntity.getPos()) && c1.others.get(i).getRight() == c2.terminal.terminalId) {
                        c1.others.remove(i);
                        break;
                    }
                }
                for(int i = 0; i < c2.others.size(); ++i) {
                    if(c2.others.get(i).getLeft().equals(c1.terminal.blockEntity.getPos()) && c2.others.get(i).getRight() == c1.terminal.terminalId) {
                        c2.others.remove(i);
                        break;
                    }
                }
                c1.terminal.blockEntity.markDirty();
                c2.terminal.blockEntity.markDirty();
                ((IIotaWireConnectorHost) c1.terminal.blockEntity).sync();
                ((IIotaWireConnectorHost) c2.terminal.blockEntity).sync();
                //world.updateListeners(pos1, world.getBlockState(pos1), world.getBlockState(pos1), 2);
                //world.updateListeners(pos2, world.getBlockState(pos2), world.getBlockState(pos2), 2);
                return true;
            }
        }
        return false;
    }
}
