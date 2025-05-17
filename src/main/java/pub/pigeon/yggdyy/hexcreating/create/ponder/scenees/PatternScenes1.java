package pub.pigeon.yggdyy.hexcreating.create.ponder.scenees;

import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import net.minecraft.util.math.Vec3d;
import pub.pigeon.yggdyy.hexcreating.create.ponder.HexPonderUtils;

public class PatternScenes1 {
    public static void introduction(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pattern_introduction", "Introduction to draw patterns");
        scene.configureBasePlate(0, 0, 5);
        //scene.showBasePlate();
        scene.rotateCameraY(35);
        scene.idle(20);
        var patternTest = new HexPonderUtils.PatternDisplay(scene, util, new Vec3d(3, 3, 0), new Vec3d(-1, 0, 0), new Vec3d(0, 0.5 * Math.sqrt(2), 0.5 * Math.sqrt(2)), HexPattern.fromAngles("qaq", HexDir.NORTH_EAST));
        patternTest.show(HexPonderUtils.A_LONG_TIME, 20);
    }
    public static void reset(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pattern_introduction_reset", "How to reset the casting HUD");
    }
    public static void locateSelf(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pattern_locate_self", "How to get yourself and your location");
    }
    public static void locateBlock(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pattern_locate_block", "How to get location of block");
    }
    public static void locateEntity(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pattern_locate_entity", "How to get entity and its location");
    }
    public static void numberConstants(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pattern_constants_number", "Number Constants");
    }
    public static void vectorConstants(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pattern_constants_vector", "Vector Constants");
    }
    public static void booleanConstants(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pattern_constants_boolean", "Boolean Constants");
    }
    public static void calculate(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pattern_calculate", "Calculate Iotas");
    }
    public static void edifyTreePattern(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pattern_spell_edifytree", "Pattern of EdifyTree");
    }
    public static void edifyTreePractice(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pattern_spell_edifytree_practice", "Practice of EdifyTree");
    }
}
