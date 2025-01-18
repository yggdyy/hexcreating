package pub.pigeon.yggdyy.hexcreating.blocks.iotareader;

import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.item.HexHolderItem;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import at.petrak.hexcasting.common.lib.HexSounds;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaNetProcessor;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaPack;
import pub.pigeon.yggdyy.hexcreating.api.iotanet.IotaTerminal;

import java.util.List;

public class IotaReadBehaviour extends BeltProcessingBehaviour {
    public IotaReaderBlockEntity reader;
    public long needReadTime = 0, haveReadTime = 0; // the process of read iota, not exactly "time"
    public static String NEED_READ_TIME_KEY = "need_read_time", HAVE_READ_TIME_KEY = "have_read_time";
    public Iota targetIota = null;
    public static String TARGET_IOTA_KEY = "need_read_iota";

    public IotaReadBehaviour(IotaReaderBlockEntity be) {
        super(be);
        reader = be;
        whenItemEnters(this::itemEnter);
        whileItemHeld(this::itemHeld);
    }

    private ProcessingResult itemHeld(TransportedItemStack transportedItemStack, TransportedItemStackHandlerBehaviour handler) {
        //handler.getWorld().getPlayers().get(0).sendMessage(Text.literal(handler.getWorld().getTime() + ""));
        if(reader.getWorld() == null) return ProcessingResult.PASS;
        haveReadTime += Math.abs(reader.getSpeed());

        if(haveReadTime >= needReadTime) {
            var toList = reader.getCanSendIotaPackToPos();
            var pack = IotaPack.createNew(this.targetIota);
            for(var p : toList) {
                IotaNetProcessor.trySendIotaPack(pack.copyWithSameId(), new IotaTerminal(blockEntity, 0), p, (ServerWorld) blockEntity.getWorld());
            }
            this.haveReadTime = this.needReadTime = 0L;
            this.targetIota = null;
            reader.getWorld().playSound(null, reader.getPos(), HexSounds.READ_LORE_FRAGMENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
            reader.sync();
            return ProcessingResult.PASS;
        }
        reader.sync();
        return ProcessingResult.HOLD;
    }
    private ProcessingResult itemEnter(@NotNull TransportedItemStack transportedItemStack, TransportedItemStackHandlerBehaviour handler) {
        if(transportedItemStack.stack.getItem() instanceof HexHolderItem item) {
            if(item.hasHex(transportedItemStack.stack)) {
                List<Iota> l = item.getHex(transportedItemStack.stack, (ServerWorld) handler.getWorld());
                if(l.size() == 0) return ProcessingResult.PASS;
                else if(l.size() == 1) {
                    this.targetIota = l.get(0);
                    this.haveReadTime = 0;
                    this.needReadTime = getIotaReadNeedTimeArg() * 128L * this.targetIota.size();
                } else {
                    this.targetIota = new ListIota(l);
                    this.haveReadTime = 0;
                    this.needReadTime = getIotaReadNeedTimeArg() * 128L * this.targetIota.size();
                }
                reader.getWorld().playSound(null, reader.getPos(), HexSounds.CAST_HERMES, SoundCategory.BLOCKS, 1.0f, 1.0f);
                reader.sync();
                return ProcessingResult.HOLD;
            }
        } else if(transportedItemStack.stack.getItem() instanceof IotaHolderItem item) {
            var iota = item.readIota(transportedItemStack.stack, (ServerWorld) handler.getWorld());
            if(iota == null) return ProcessingResult.PASS;
            this.targetIota = iota;
            this.haveReadTime = 0L;
            this.needReadTime = getIotaReadNeedTimeArg() * 128L * this.targetIota.size();
            reader.getWorld().playSound(null, reader.getPos(), HexSounds.CAST_THOTH, SoundCategory.BLOCKS, 1.0f, 1.0f);
            reader.sync();
            return ProcessingResult.HOLD;
        }
        return ProcessingResult.PASS;
    }

    // an argument determine the read time of iota, will be in config file in future
    public long getIotaReadNeedTimeArg() {
        return 20L;
    }

    @Override
    public void read(NbtCompound nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        this.needReadTime = nbt.contains(NEED_READ_TIME_KEY)? nbt.getLong(NEED_READ_TIME_KEY) : 0;
        this.haveReadTime = nbt.contains(HAVE_READ_TIME_KEY)? nbt.getLong(HAVE_READ_TIME_KEY) : 0;
        if(!clientPacket) {
            if(nbt.contains(TARGET_IOTA_KEY)) this.targetIota = IotaType.deserialize(nbt.getCompound(TARGET_IOTA_KEY), (ServerWorld) reader.getWorld());
        }
    }

    @Override
    public void write(NbtCompound nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        nbt.putLong(NEED_READ_TIME_KEY, needReadTime);
        nbt.putLong(HAVE_READ_TIME_KEY, haveReadTime);
        if(!clientPacket) {
            if(this.targetIota != null) nbt.put(TARGET_IOTA_KEY, IotaType.serialize(this.targetIota));
        }
    }

    public int particleRenderTimer = 4;
    public static double PARTICLE_RENDER_BOTTOM_OFFSET = -1.5, PARTICLE_RENDER_TOP_OFFSET = -1.2;
    @Override
    public void tick() {
        if(reader.getWorld() != null && reader.getWorld().isClient) {
            if(needReadTime <= 0) {
                particleRenderTimer = 4;
            } else {
                ++particleRenderTimer;
                if(particleRenderTimer >= 5) {
                    particleRenderTimer = 0;
                    var p = reader.getPos().toCenterPos();
                    double y = (haveReadTime * 1.0 / needReadTime) * (PARTICLE_RENDER_TOP_OFFSET - PARTICLE_RENDER_BOTTOM_OFFSET) + p.y + PARTICLE_RENDER_BOTTOM_OFFSET;
                    reader.getWorld().addParticle(
                            ParticleTypes.WITCH,
                            p.x, y, p.z,
                            0, 0, 0
                    );

                }
            }
        }
    }



}
