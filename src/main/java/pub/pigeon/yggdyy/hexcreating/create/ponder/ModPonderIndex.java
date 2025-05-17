package pub.pigeon.yggdyy.hexcreating.create.ponder;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;
import pub.pigeon.yggdyy.hexcreating.create.ponder.scenees.HexIntroductionScenes;
import pub.pigeon.yggdyy.hexcreating.create.ponder.scenees.PatternScenes1;
import pub.pigeon.yggdyy.hexcreating.items.ModItems;

public class ModPonderIndex {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(HexcreatingMain.MOD_ID);
    public static void init() {
        HELPER.forComponents(ModItems.THOUGHT_KEY_HEXCASTING_INTRODUCTION).
                addStoryBoard("hex/introduction", HexIntroductionScenes::hexcasting);
        HELPER.forComponents(ModItems.THOUGHT_KEY_BASIC_IOTA)
                .addStoryBoard("hex/empty_5_5", HexIntroductionScenes::basicIotaEntity)
                .addStoryBoard("hex/empty_5_5", HexIntroductionScenes::basicIotaNumber)
                .addStoryBoard("hex/empty_5_5", HexIntroductionScenes::basicIotaVector)
                .addStoryBoard("hex/empty_5_5", HexIntroductionScenes::baseIotaBool);
        HELPER.forComponents(ModItems.THOUGHT_KEY_VECTOR)
                .addStoryBoard("hex/empty_5_5", HexIntroductionScenes::vector);
        HELPER.forComponents(ModItems.THOUGHT_KEY_STACK)
                .addStoryBoard("hex/empty_5_5_10", HexIntroductionScenes::stack);

        HELPER.forComponents(ModItems.THOUGHT_KEY_PATTERN_INTRODUCTION)
                .addStoryBoard("hex/empty_5_5", PatternScenes1::introduction)
                .addStoryBoard("hex/empty_5_5", PatternScenes1::reset);
        HELPER.forComponents(ModItems.THOUGHT_KEY_PATTERN_LOCATE)
                .addStoryBoard("hex/empty_5_5", PatternScenes1::locateSelf)
                .addStoryBoard("hex/empty_5_5", PatternScenes1::locateBlock)
                .addStoryBoard("hex/empty_5_5", PatternScenes1::locateEntity);
        HELPER.forComponents(ModItems.THOUGHT_KEY_PATTERN_CONSTANTS)
                .addStoryBoard("hex/empty_5_5", PatternScenes1::numberConstants)
                .addStoryBoard("hex/empty_5_5", PatternScenes1::vectorConstants)
                .addStoryBoard("hex/empty_5_5", PatternScenes1::booleanConstants);
        HELPER.forComponents(ModItems.THOUGHT_KEY_PATTERN_CALCULATE)
                .addStoryBoard("hex/empty_5_5", PatternScenes1::calculate);
        HELPER.forComponents(ModItems.THOUGHT_KEY_PATTERN_SPELL_1)
                .addStoryBoard("hex/empty_5_5", PatternScenes1::edifyTreePattern)
                .addStoryBoard("hex/empty_5_5", PatternScenes1::edifyTreePractice);
    }
}
