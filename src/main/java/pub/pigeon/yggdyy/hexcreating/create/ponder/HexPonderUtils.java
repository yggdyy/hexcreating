package pub.pigeon.yggdyy.hexcreating.create.ponder;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import at.petrak.hexcasting.common.lib.HexBlocks;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.element.TextWindowElement;
import com.simibubi.create.foundation.ponder.instruction.TextInstruction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.mixins.SceneBuilderAccessor;
import pub.pigeon.yggdyy.hexcreating.mixins.TextWindowElementAccessor;

import java.util.List;

public class HexPonderUtils {
    public static class IotaDisplay {
        public SceneBuilder scene;
        public SceneBuildingUtil util;
        public Iota iota;
        @Nullable private BlockPos nowPos;
        public IotaDisplay(SceneBuilder scene, SceneBuildingUtil util, Iota iota) {
            this.scene = scene;
            this.util = util;
            this.iota = iota;
        }
        public void setPos(BlockPos pos) {this.nowPos = pos;}
        public void showIota(int predictedDuration) {
            if(nowPos == null) return;
            scene.world.setBlock(nowPos, getBookshelfState(), false);
            scene.world.modifyBlockEntity(nowPos, BlockEntityAkashicBookshelf.class, be -> {
                be.setNewMapping(getPattern(), iota);
            });
            scene.world.showSection(util.select.position(nowPos), Direction.DOWN);

            TextWindowElement iotaText = new TextWindowElement();
            var iotaTextBuilder = iotaText.new Builder(((SceneBuilderAccessor)scene).getScene());
            iotaTextBuilder.pointAt(util.vector.blockSurface(nowPos, Direction.WEST)).placeNearTarget().attachKeyFrame().colored(getColor());
            ((TextWindowElementAccessor)iotaText).setTextGetter(() -> iota.display().getString());
            scene.addInstruction(new TextInstruction(iotaText, predictedDuration));
        }
        public void hideIota() {
            if(nowPos == null) return;
            scene.world.setBlock(nowPos, Blocks.AIR.getDefaultState(), false);
        }

        private BlockState getBookshelfState() {
            return HexBlocks.AKASHIC_BOOKSHELF.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH);
        }
        private HexPattern getPattern() {
            return HexPattern.fromAngles("w", HexDir.EAST);
        }
        private PonderPalette getColor() {
            return PonderPalette.WHITE;
        }
    }
    public static class StackDisplay {

    }
    public static class ArrowDisplay {
        public SceneBuilder scene;
        public SceneBuildingUtil util;
        public Vec3d startPos = Vec3d.ZERO, endPos = new Vec3d(0, 1, 0);
        public double headSize = 1;
        public PonderPalette baseColor = PonderPalette.WHITE, headColor = PonderPalette.WHITE;
        public ArrowDisplay(SceneBuilder scene, SceneBuildingUtil util) {
            this.scene = scene; this.util = util;
        }
        public ArrowDisplay start(Vec3d vec) {
            this.startPos = vec;
            return this;
        }
        public ArrowDisplay end(Vec3d vec) {
            this.endPos = vec;
            return this;
        }
        public ArrowDisplay size(double head) {
            this.headSize = head;
            return this;
        }
        public ArrowDisplay color(PonderPalette base, PonderPalette head) {
            this.baseColor = base;
            this.headColor = head;
            return this;
        }
        public void show(int duration) {
            scene.overlay.showLine(baseColor, startPos, endPos, duration);
            Vec3d delta = endPos.add(startPos.multiply(-1));
            Vec3d f = delta.crossProduct(new Vec3d(0, 1, 0)).normalize().multiply(headSize);
            if(delta.normalize().dotProduct(new Vec3d(0, 1, 0)) == 1) f = new Vec3d(1, 0, 0).multiply(headSize);
            Vec3d preHead = delta.multiply(headSize / delta.length() * -1);
            Vec3d head1 = preHead.add(f), head2 = preHead.add(f.multiply(-1));
            scene.overlay.showLine(headColor, endPos.add(head1), endPos, duration);
            scene.overlay.showLine(headColor, endPos, endPos.add(head2), duration);
        }
    }
}
