package pub.pigeon.yggdyy.hexcreating.cast.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.cast.ModIotaTypes;

public class TranslateIota extends Iota {
    public TranslateIota(@NotNull String translationKey) {
        super(ModIotaTypes.TRANSLATE, translationKey);
    }
    public String getTranslateKey() {
        return (this.payload instanceof String str)? str : "hexcreating.null";
    }

    @Override
    public boolean isTruthy() {
        return false;
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return false;
    }

    @Override
    public @NotNull NbtElement serialize() {
        return NbtString.of(getTranslateKey());
    }
    public static TranslateIota deserialize(NbtElement tag) {
        return new TranslateIota(tag.getType() == NbtElement.STRING_TYPE? tag.asString() : "hexcreating.null");
    }

    public static IotaType<TranslateIota> TYPE = new IotaType<>() {
        @Nullable
        @Override
        public TranslateIota deserialize(NbtElement tag, ServerWorld world) throws IllegalArgumentException {
            return TranslateIota.deserialize(tag);
        }

        @Override
        public Text display(NbtElement tag) {
            return Text.translatable(TranslateIota.deserialize(tag).getTranslateKey());
        }

        @Override
        public int color() {
            return 0xff_55ffff;
        }
    };
}
