package pub.pigeon.yggdyy.hexcreating.libs;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import pub.pigeon.yggdyy.hexcreating.blocks.connector.IotaWireConnectorBlock;
import pub.pigeon.yggdyy.hexcreating.blocks.connector.IotaWireConnectorBlockEntity;

import java.util.List;

public class IotaWireConnectorPropertyHelper {
    public static BooleanProperty getPropertyByDir(Direction dir) {
        if(dir == Direction.UP) return IotaWireConnectorBlock.UP;
        else if(dir == Direction.DOWN) return IotaWireConnectorBlock.DOWN;
        else if(dir == Direction.NORTH) return IotaWireConnectorBlock.NORTH;
        else if(dir == Direction.SOUTH) return IotaWireConnectorBlock.SOUTH;
        else if(dir == Direction.WEST) return IotaWireConnectorBlock.WEST;
        else if(dir == Direction.EAST) return IotaWireConnectorBlock.EAST;
        return IotaWireConnectorBlock.UP;
    }

    public static List<BooleanProperty> getAllProperty() {
        return List.of(IotaWireConnectorBlock.UP, IotaWireConnectorBlock.DOWN, IotaWireConnectorBlock.NORTH, IotaWireConnectorBlock.SOUTH, IotaWireConnectorBlock.WEST, IotaWireConnectorBlock.EAST);
    }

    public static VoxelShape getVoxelBox(BooleanProperty p) {
        if(p == IotaWireConnectorBlock.UP) return IotaWireConnectorBlock.UP_BOX;
        else if(p == IotaWireConnectorBlock.DOWN) return IotaWireConnectorBlock.DOWN_BOX;
        else if(p == IotaWireConnectorBlock.NORTH) return IotaWireConnectorBlock.NORTH_BOX;
        else if(p == IotaWireConnectorBlock.SOUTH) return IotaWireConnectorBlock.SOUTH_BOX;
        else if(p == IotaWireConnectorBlock.WEST) return IotaWireConnectorBlock.WEST_BOX;
        else if(p == IotaWireConnectorBlock.EAST) return IotaWireConnectorBlock.EAST_BOX;
        return IotaWireConnectorBlock.UP_BOX;
    }

    public static int getTerminalIdByProperty(BooleanProperty p) {
        if(p == IotaWireConnectorBlock.UP) return 0;
        else if(p == IotaWireConnectorBlock.DOWN) return 1;
        else if(p == IotaWireConnectorBlock.NORTH) return 2;
        else if(p == IotaWireConnectorBlock.SOUTH) return 3;
        else if(p == IotaWireConnectorBlock.WEST) return 4;
        else if(p == IotaWireConnectorBlock.EAST) return 5;

        return 0;
    }
}
