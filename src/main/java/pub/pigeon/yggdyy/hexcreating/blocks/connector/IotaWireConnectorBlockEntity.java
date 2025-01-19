package pub.pigeon.yggdyy.hexcreating.blocks.connector;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IIotaTerminalHost;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaNetProcessor;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaPack;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaTerminal;
import pub.pigeon.yggdyy.hexcreating.api.iotawire.IIotaWireConnectorHost;
import pub.pigeon.yggdyy.hexcreating.api.iotawire.IotaWireConnector;
import pub.pigeon.yggdyy.hexcreating.api.iotawire.IotaWireProcessor;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;

import java.util.List;

public class IotaWireConnectorBlockEntity extends SmartBlockEntity implements IIotaWireConnectorHost {
    public IotaTerminal up, down, north, south, west, east;
    public IotaWireConnector upC, downC, northC, southC, westC, eastC;

    public static List<Vec3i> CONNECTOR_TO_POS = List.of(
            Direction.UP.getVector(),
            Direction.DOWN.getVector(),
            Direction.NORTH.getVector(),
            Direction.SOUTH.getVector(),
            Direction.WEST.getVector(),
            Direction.EAST.getVector()
    );

    public IotaWireConnectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IOTA_WIRE_CONNECTOR, pos, state);
        up = new IotaTerminal(this, 0);
        down = new IotaTerminal(this, 1);
        north = new IotaTerminal(this, 2);
        south = new IotaTerminal(this, 3);
        west = new IotaTerminal(this, 4);
        east = new IotaTerminal(this, 5);
        upC = new IotaWireConnector(up, IotaWireConnectorBlock.UP_BOX.getBoundingBox());
        downC = new IotaWireConnector(down, IotaWireConnectorBlock.DOWN_BOX.getBoundingBox());
        northC = new IotaWireConnector(north, IotaWireConnectorBlock.NORTH_BOX.getBoundingBox());
        southC = new IotaWireConnector(south, IotaWireConnectorBlock.SOUTH_BOX.getBoundingBox());
        westC = new IotaWireConnector(west, IotaWireConnectorBlock.WEST_BOX.getBoundingBox());
        eastC = new IotaWireConnector(east, IotaWireConnectorBlock.EAST_BOX.getBoundingBox());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    protected void write(NbtCompound tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        upC.writeNbt(tag);
        downC.writeNbt(tag);
        northC.writeNbt(tag);
        southC.writeNbt(tag);
        westC.writeNbt(tag);
        eastC.writeNbt(tag);
    }

    @Override
    protected void read(NbtCompound tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        upC.readNbt(tag);
        downC.readNbt(tag);
        northC.readNbt(tag);
        southC.readNbt(tag);
        westC.readNbt(tag);
        eastC.readNbt(tag);
    }

    @Override
    public void sync() {
        this.markDirty();
        if (world != null) {
            world.updateListeners(getPos(), world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }

    @Override
    public List<IotaTerminal> getTerminals() {
        if(this.getWorld() == null) return List.of();
        List<IotaTerminal> ret = new java.util.ArrayList<>(List.of());
        BlockState state = this.getWorld().getBlockState(this.getPos());
        if(state.get(IotaWireConnectorBlock.UP)) ret.add(up);
        if(state.get(IotaWireConnectorBlock.DOWN)) ret.add(down);
        if(state.get(IotaWireConnectorBlock.NORTH)) ret.add(north);
        if(state.get(IotaWireConnectorBlock.SOUTH)) ret.add(south);
        if(state.get(IotaWireConnectorBlock.WEST)) ret.add(west);
        if(state.get(IotaWireConnectorBlock.EAST)) ret.add(east);
        return ret;
    }

    @Override
    public boolean tryReceiveIotaPack(IotaPack pack, IotaTerminal from, IotaTerminal to) {
        //world.getPlayers().get(0).sendMessage(Text.literal(pos.toString()));
        var l = getIotaWireConnectors();
        for(var c : l) {
            if(c.terminal.terminalId == to.terminalId) {
                to.confirmAcceptIotaPack(pack);
                c.sendIotaPackToAll(pack);
                IotaNetProcessor.trySendIotaPack(pack.copyWithSameId(), c.terminal, pos.add(CONNECTOR_TO_POS.get(c.terminal.terminalId)), (ServerWorld) world);
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable IotaTerminal getDefaultTerminal(IotaTerminal from) {
        if(world == null) return null;
        BlockPos fPos = from.blockEntity.getPos();
        BlockState state = world.getBlockState(pos);
        if(fPos.equals(pos.add(0, 1, 0)) && state.get(IotaWireConnectorBlock.UP)) return up;
        else if(fPos.equals(pos.add(0, -1, 0)) && state.get(IotaWireConnectorBlock.DOWN)) return down;
        else if(fPos.equals(pos.add(0, 0, -1)) && state.get(IotaWireConnectorBlock.NORTH)) return north;
        else if(fPos.equals(pos.add(0, 0, 1)) && state.get(IotaWireConnectorBlock.SOUTH)) return south;
        else if(fPos.equals(pos.add(-1, 0, 0)) && state.get(IotaWireConnectorBlock.WEST)) return west;
        else if(fPos.equals(pos.add(1, 0, 0)) && state.get(IotaWireConnectorBlock.EAST)) return east;
        return null;
    }

    @Override
    public List<IotaWireConnector> getIotaWireConnectors() {
        if(this.getWorld() == null) return List.of();
        List<IotaWireConnector> ret = new java.util.ArrayList<>(List.of());
        BlockState state = this.getWorld().getBlockState(this.getPos());
        if(state.get(IotaWireConnectorBlock.UP)) ret.add(upC);
        if(state.get(IotaWireConnectorBlock.DOWN)) ret.add(downC);
        if(state.get(IotaWireConnectorBlock.NORTH)) ret.add(northC);
        if(state.get(IotaWireConnectorBlock.SOUTH)) ret.add(southC);
        if(state.get(IotaWireConnectorBlock.WEST)) ret.add(westC);
        if(state.get(IotaWireConnectorBlock.EAST)) ret.add(eastC);
        return ret;
    }

    @Override
    public void onDataPacket(ClientConnection connection, BlockEntityUpdateS2CPacket packet) {
        super.onDataPacket(connection, packet);
        //this.read(packet.getNbt(), true);
    }

    @Override
    public BlockEntityUpdateS2CPacket getUpdatePacket() {
        return super.getUpdatePacket();
    }
}
