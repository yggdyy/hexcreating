package pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities

import at.petrak.hexcasting.api.utils.hasInt
import com.simibubi.create.content.kinetics.base.KineticBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities

class AnalyticalEngineKineticInterfaceBlockEntity(blockPos: BlockPos, blockState: BlockState): KineticBlockEntity(ModBlockEntities.ANALYTICAL_ENGINE_KINETIC_INTERFACE, blockPos, blockState) {
    override fun write(pTag: NbtCompound, clientPacket: Boolean) {
        super.write(pTag, clientPacket)
        this.saveModData(pTag)
    }

    override fun read(pTag: NbtCompound, clientPacket: Boolean) {
        super.read(pTag, clientPacket)
        this.loadModData(pTag)
    }

    override fun toInitialChunkDataNbt(): NbtCompound {
        val tag = super.toInitialChunkDataNbt()
        this.saveModData(tag)
        return tag
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener?> {
        return BlockEntityUpdateS2CPacket.create(this)
    }

    fun sync() {
        this.markDirty()
        world!!.updateListeners(getPos(), cachedState, cachedState, 3)
    }

    var controllerPos: BlockPos? = null
    val CONTROLLER_POS_KEY = "controller_pos"

    fun loadModData(tag: NbtCompound) {
        if(tag.hasInt(CONTROLLER_POS_KEY + "_x")) {
            controllerPos = BlockPos(
                    tag.getInt(CONTROLLER_POS_KEY + "_x"),
                    tag.getInt(CONTROLLER_POS_KEY + "_y"),
                    tag.getInt(CONTROLLER_POS_KEY + "_z")
            )
        } else controllerPos = null;
    }
    fun saveModData(tag: NbtCompound) {
        if(controllerPos != null) {
            tag.putInt(CONTROLLER_POS_KEY + "_x", controllerPos!!.x)
            tag.putInt(CONTROLLER_POS_KEY + "_y", controllerPos!!.y)
            tag.putInt(CONTROLLER_POS_KEY + "_z", controllerPos!!.z)
        }
    }
    fun tellControllerBroken() {
        if(controllerPos != null) {
            val ce: AnalyticalEngineControllerBlockEntity? = world?.getBlockEntity(controllerPos) as? AnalyticalEngineControllerBlockEntity
            if(ce == null) {
                controllerPos = null
                return
            } else {
                controllerPos = pos
                ce.updateStructure()
            }
            sync()
        }
    }

    override fun calculateStressApplied(): Float {
        //return super.calculateStressApplied()
        if(controllerPos == null) return 1f;
        else {
            val c: AnalyticalEngineControllerBlockEntity? = world?.getBlockEntity(controllerPos) as? AnalyticalEngineControllerBlockEntity
            if(c == null) {
                controllerPos = null
                return 1f
            } else {
                return c.getStressApplied()
            }
        }
    }
    //fun forgetController() {this.controllerPos = null;}
}