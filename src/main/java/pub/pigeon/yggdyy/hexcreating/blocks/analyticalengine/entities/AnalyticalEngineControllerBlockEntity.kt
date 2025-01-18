package pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities

import at.petrak.hexcasting.api.block.HexBlockEntity

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.PatternIota

import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.hasInt


import net.minecraft.block.BlockState

import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.blocks.AnalyticalEngineALUBlock
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.blocks.AnalyticalEngineControllerBlock
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.blocks.AnalyticalEngineKineticInterfaceBlock
import kotlin.math.abs
import kotlin.math.floor


class AnalyticalEngineControllerBlockEntity(blockPos: BlockPos, blockState: BlockState): HexBlockEntity(ModBlockEntities.ANALYTICAL_ENGINE_CONTROLLER, blockPos, blockState){
    public var patterns: List<HexPattern> = listOf()
    public val PATTERNS_KEY = "hex_script"
    public var test_num: Int = 114
    public var posALUs: List<BlockPos> = listOf() //the ALUs
    public val POS_ALUS_KEY = "pos_alus"
    public var posKinetic: BlockPos? = null //null means there is no kinetic interface bind with this controller
    public val POS_KINETIC_KEY = "pos_kinetic"

    override fun saveModData(tag: NbtCompound) {
        tag.putInt("test_num", test_num)

        for(i in 0 until patterns.count()) {
            tag.put(
                    PATTERNS_KEY + "_" + i.toString(),
                    patterns[i].serializeToNBT()
            )
        }
        tag.putInt(PATTERNS_KEY + "_length", patterns.count())

        for(i in 0 until posALUs.count()) {
            tag.putInt(
                    POS_ALUS_KEY + "_" + i.toString() + "_x",
                    posALUs[i].x
            )
            tag.putInt(
                    POS_ALUS_KEY + "_" + i.toString() + "_y",
                    posALUs[i].y
            )
            tag.putInt(
                    POS_ALUS_KEY + "_" + i.toString() + "_z",
                    posALUs[i].z
            )
        }
        tag.putInt(POS_ALUS_KEY + "_length", posALUs.count())

        if(posKinetic != null) {
            tag.putInt(POS_KINETIC_KEY + "_x", posKinetic!!.x)
            tag.putInt(POS_KINETIC_KEY + "_y", posKinetic!!.y)
            tag.putInt(POS_KINETIC_KEY + "_z", posKinetic!!.z)
        }
    }

    override fun loadModData(tag: NbtCompound) {
        test_num = tag.getInt("test_num")

        var new_patterns: MutableList<HexPattern> = mutableListOf()
        var l: Int = 0
        if(tag.hasInt(PATTERNS_KEY + "_length")) l = tag.getInt(PATTERNS_KEY + "_length")
        else l = 0;
        for(i in 0 until l) {
            new_patterns.add(HexPattern.fromNBT(tag.getCompound(PATTERNS_KEY + "_" + i.toString())))
        }
        patterns = new_patterns

        var new_pos_alus: MutableList<BlockPos> = mutableListOf()
        if(tag.hasInt(POS_ALUS_KEY + "_length")) l = tag.getInt(POS_ALUS_KEY + "_length")
        else l = 0;
        for(i in 0 until l) {
            new_pos_alus.add(BlockPos(
                    tag.getInt(POS_ALUS_KEY + "_" + i.toString() + "_x"),
                    tag.getInt(POS_ALUS_KEY + "_" + i.toString() + "_y"),
                    tag.getInt(POS_ALUS_KEY + "_" + i.toString() + "_z")
            ))
        }
        posALUs = new_pos_alus

        if(tag.hasInt(POS_KINETIC_KEY + "_x")) posKinetic = BlockPos(
                tag.getInt(POS_KINETIC_KEY + "_x"),
                tag.getInt(POS_KINETIC_KEY + "_y"),
                tag.getInt(POS_KINETIC_KEY + "_z")
        ) else posKinetic = null;
    }

    public fun tryApplyHex(script: List<Iota>?): Boolean {
        sync()
        this.test_num++
        if(script == null) return false;
        var new_patterns: MutableList<HexPattern> = mutableListOf()
        for(i in script) {
            if(i.executable()) {
                new_patterns.add((i as PatternIota).pattern)
                //i will have this written like this for some times, since i haven't discovered how to write this properly
            } else {
                return false
            }
        }
        patterns = new_patterns
        return true
    }

    override fun toString(): String {
        var nbt: NbtCompound = NbtCompound()
        this.writeNbt(nbt)
        return nbt.asString()
    }



    //regenerate pos_alus and pos_kinetic
    //should only run on server
    public fun updateStructure() {
        if(world!!.isClient) return
        unboundStructure()
        val dfsExecutor: DFSExecutor = DFSExecutor(MAX_DFS_CNT, pos, world as ServerWorld)
        dfsExecutor.startDFS()
        posALUs = dfsExecutor.newALUsPos
        posKinetic = dfsExecutor.newKineticPos
        sync()
    }
    public val MAX_DFS_CNT: Int = 100000
    private class DFSExecutor(val maxDFSCnt: Int, val startPos: BlockPos, val world: ServerWorld) {
        var newALUsPos: MutableList<BlockPos> = mutableListOf()
        var newKineticPos: BlockPos? = null
        var cnt = 0;

        fun startDFS() {dfs(startPos);}
        private fun dfs(nowPos: BlockPos) {
            if(cnt >= maxDFSCnt) return

            if(nowPos != startPos) {
                if(world.getBlockState(nowPos).isOf(ModBlocks.ANALYTICAL_ENGINE_ALU)) {
                    val e: AnalyticalEngineALUBlockEntity = world.getBlockEntity(nowPos) as AnalyticalEngineALUBlockEntity
                    if(e.controllerPos == null) {
                        e.controllerPos = startPos
                        e.sync()
                        newALUsPos.add(nowPos)
                    } else return;
                } else if(newKineticPos == null && world.getBlockState(nowPos).isOf(ModBlocks.ANALYTICAL_ENGINE_KINETIC_INTERFACE)) {
                    val e: AnalyticalEngineKineticInterfaceBlockEntity = world.getBlockEntity(nowPos) as AnalyticalEngineKineticInterfaceBlockEntity
                    if(e.controllerPos == null) {
                        e.controllerPos = startPos
                        e.sync()
                        newKineticPos = nowPos
                    } else return
                } else return
            }

            ++cnt;

            val deltaVecs: List<Vec3i> = listOf(
                    Vec3i(0, 0, 1),
                    Vec3i(0, 1, 0),
                    Vec3i(1, 0, 0),
                    Vec3i(0, 0, -1),
                    Vec3i(0, -1, 0),
                    Vec3i(-1, 0, 0)
            )
            for(deltaVec in deltaVecs) {
                val nxtPos = nowPos.add(deltaVec)
                if(!world.isOutOfHeightLimit(nxtPos)) {
                    dfs(nxtPos)
                }
            }
        }
    }
    fun unboundStructure() {
        for(alu in posALUs) {
            val e: AnalyticalEngineALUBlockEntity? = world?.getBlockEntity(alu) as? AnalyticalEngineALUBlockEntity
            if(e != null && e.controllerPos == pos) {e.controllerPos = null; e.sync();}
        }
        posALUs = listOf()

        if(posKinetic != null) {
            val e: AnalyticalEngineKineticInterfaceBlockEntity? = world?.getBlockEntity(posKinetic) as? AnalyticalEngineKineticInterfaceBlockEntity
            if(e != null && e.controllerPos == pos) {e.controllerPos = null; e.sync();}
        }
        posKinetic = null;

        sync()
    }

    fun getStressApplied(): Float {
        var ret: Float = 1f + posALUs.count()
        if(posKinetic != null) ret += 1f
        return ret
    }

    fun canExecute(): Boolean {
        if(posKinetic == null) return false
        val ke: AnalyticalEngineKineticInterfaceBlockEntity? = world?.getBlockEntity(posKinetic) as? AnalyticalEngineKineticInterfaceBlockEntity
        if(ke == null || abs(ke.speed) < getRequiredKineticSpeed()) return false
        val l = patterns.count()
        val c = getControllerHexCapacity() + getKineticInterfaceHexCapacity() + getALUHexCapacity() * posALUs.count()
        return l <= c
    }

    //i will have the following number adjustable in config file in future
    fun getALUHexCapacity(): Int {
        return 10
    }
    fun getControllerHexCapacity(): Int {
        return 5
    }
    fun getKineticInterfaceHexCapacity(): Int {
        return 5
    }
    fun getRequiredKineticSpeed(): Float {
        return 64f
    }

    //this is different from the create's "speed", actually it is abs(speed)
    fun getKineticSpeed(): Float {
        if(posKinetic == null) return 0f
        val e: AnalyticalEngineKineticInterfaceBlockEntity? = world?.getBlockEntity(posKinetic) as? AnalyticalEngineKineticInterfaceBlockEntity
        if(e == null) {
            posKinetic = null
            return 0f
        }
        return abs(e.speed)
    }

    companion object {
        @JvmStatic
        fun tick(
            world: World,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: AnalyticalEngineControllerBlockEntity
        ) {
            //if(world.time % 10 == 0L) world.players[0].sendMessage(Text.literal("tick!"))
            blockEntity.tickSelf(world, blockPos, blockState)
        }
    }

    fun tickSelf(world: World, blockPos: BlockPos, blockState: BlockState) {
        val speed: Float = getKineticSpeed()
        if(speed < getRequiredKineticSpeed()) return
        val timer: Int = floor(2048f / speed).toInt()
        if(world.time % timer != 0L) return

        AnalyticalEngineControllerBlock.marchRenderStage(world, blockPos, blockState)

        AnalyticalEngineKineticInterfaceBlock.marchRenderStage(world, posKinetic!!, world.getBlockState(posKinetic))

        for(p in posALUs) {
            val stateThere = world.getBlockState(p)
            if(stateThere.isOf(ModBlocks.ANALYTICAL_ENGINE_ALU)) {
                AnalyticalEngineALUBlock.marchRenderStage(world, p, stateThere)
            }
        }
    }
}