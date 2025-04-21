package pub.pigeon.yggdyy.hexcreating.create.ponder;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.api.casting.math.HexAngle;
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
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.mixins.SceneBuilderAccessor;
import pub.pigeon.yggdyy.hexcreating.mixins.TextWindowElementAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            iotaTextBuilder.pointAt(util.vector.blockSurface(nowPos, Direction.NORTH).add(0, -0.25, 0)).placeNearTarget().colored(getColor());
            ((TextWindowElementAccessor)iotaText).setTextGetter(() -> iota.display().getString());
            scene.addInstruction(new TextInstruction(iotaText, predictedDuration));
        }
        public void hideIota() {
            if(nowPos == null) return;
            scene.world.hideSection(util.select.position(nowPos), Direction.UP);
            //scene.world.setBlock(nowPos, Blocks.AIR.getDefaultState(), false);
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
        public SceneBuilder scene;
        public SceneBuildingUtil util;
        private ArrayList<IotaDisplay> iotas = new ArrayList<>();
        private BlockPos basePos;

        public StackDisplay(SceneBuilder scene, SceneBuildingUtil util, BlockPos basePos) {
            this.scene = scene;
            this.util = util;
            this.basePos = basePos;
        }
        public void push(Iota iota, int textDuration) {
            BlockPos pos = basePos.up(iotas.size() + 1);
            IotaDisplay display = new IotaDisplay(scene, util, iota);
            display.setPos(pos);
            display.showIota(textDuration);
            iotas.add(display);
        }
        public void pop() {
            if(iotas.size() == 0) return;
            iotas.get(iotas.size() - 1).hideIota();
            iotas.remove(iotas.size() - 1);
        }
        public Iota top(int rId) {
            if(0 < rId && rId <= iotas.size()) {
                return iotas.get(iotas.size() - rId).iota;
            }
            return new NullIota();
        }
        public Iota top() {return top(1);}
        public Vec3d tipPos(int rId) {
            int offset = iotas.size() - rId + 1;
            return util.vector.blockSurface(basePos.up(offset), Direction.WEST).add(0, 0.25, 0);
        }
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
            if(headSize <= 0) return;
            Vec3d delta = endPos.add(startPos.multiply(-1));
            Vec3d f = delta.crossProduct(new Vec3d(0, 1, 0)).normalize().multiply(headSize);
            if(delta.normalize().dotProduct(new Vec3d(0, 1, 0)) == 1) f = new Vec3d(1, 0, 0).multiply(headSize);
            Vec3d preHead = delta.multiply(headSize / delta.length() * -1);
            Vec3d head1 = preHead.add(f), head2 = preHead.add(f.multiply(-1));
            scene.overlay.showLine(headColor, endPos.add(head1), endPos, duration);
            scene.overlay.showLine(headColor, endPos, endPos.add(head2), duration);
        }
    }
    public static class PatternDisplay {
        private SceneBuilder scene;
        private SceneBuildingUtil util;
        private Vec3d o, x, y, z;
        private List<ArrowDisplay> strokes = new ArrayList<>();

        private static double Y_DELTA = Math.sqrt(3.0) / 2.0;
        private static int M = 6;
        private static Map<Integer, Pair<Double, Double>> INDEX2OFFSET = Map.of(
                0, new Pair<>(0.5, Y_DELTA),
                1, new Pair<>(1.0, 0.0),
                2, new Pair<>(0.5, -Y_DELTA),
                3, new Pair<>(-0.5, -Y_DELTA),
                4, new Pair<>(-1.0, 0.0),
                5, new Pair<>(-0.5, Y_DELTA)
        );
        private static Map<HexDir, Integer> DIR2INDEX = Map.of(
                HexDir.NORTH_EAST, 0,
                HexDir.EAST, 1,
                HexDir.SOUTH_EAST, 2,
                HexDir.SOUTH_WEST, 3,
                HexDir.WEST, 4,
                HexDir.NORTH_WEST, 5
        );
        private static Map<HexAngle, Integer> ANGLE2INDEX = Map.of(
                HexAngle.FORWARD, 0,
                HexAngle.RIGHT, 1,
                HexAngle.RIGHT_BACK, 2,
                HexAngle.BACK, 3,
                HexAngle.LEFT_BACK, 4,
                HexAngle.LEFT, 5
        );

        public PatternDisplay(SceneBuilder scene, SceneBuildingUtil util, Vec3d origin, Vec3d xDelta, Vec3d yDelta, HexPattern pattern) {
            this.o = origin; this.x = xDelta; this.y = yDelta; this.z = x.crossProduct(y);
            this.scene = scene; this.util = util;

            Vec3d nowPos = o; int nowIndex = DIR2INDEX.get(pattern.getStartDir());
            strokes.add(new ArrowDisplay(scene, util).start(nowPos).end(nowPos.add(makeVec3(INDEX2OFFSET.get(nowIndex))).add(z.multiply(0.01))).size(x.length() / 8.0));
            nowPos = nowPos.add(makeVec3(INDEX2OFFSET.get(nowIndex)));
            for(var i : pattern.getAngles()) {
                nowIndex = (nowIndex + ANGLE2INDEX.get(i)) % M;
                strokes.add(new ArrowDisplay(scene, util).start(nowPos).end(nowPos.add(makeVec3(INDEX2OFFSET.get(nowIndex))).add(z.multiply(0.01))).size(x.length() / 8.0));
                nowPos = nowPos.add(makeVec3(INDEX2OFFSET.get(nowIndex)));
            }
        }
        private Vec3d makeVec3(double _x, double _y) {
            return x.multiply(_x).add(y.multiply(_y));
        }
        private Vec3d makeVec3(Pair<Double, Double> pair) {
            return makeVec3(pair.getLeft(), pair.getRight());
        }
        public void show(int duration, int per, List<PonderPalette> colors) {
            if(strokes.size() == 0) return;
            if(colors.size() == 0) {
                show(duration, per, List.of(PonderPalette.FAST));
                return;
            }
            if(per < 0 || per * strokes.size() >= duration) {
                show(duration, duration / strokes.size(), colors);
                return;
            }
            for(int i = 0; i < strokes.size(); ++i) {
                PonderPalette nowColor = i < colors.size()? colors.get(i) : colors.get(colors.size() - 1);
                strokes.get(i).color(nowColor, nowColor).show(duration - i * per);
            }
        }
        public void show(int duration, int per, PonderPalette color) {
            show(duration, per, List.of(color));
        }
        public void show(int duration, List<PonderPalette> colors) {
            show(duration, 0, colors);
        }
        public void show(int duration, PonderPalette color) {
            show(duration, 0, List.of(color));
        }
    }
}
