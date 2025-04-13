package pub.pigeon.yggdyy.hexcreating.create.ponder;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;
import pub.pigeon.yggdyy.hexcreating.create.ponder.scenees.HexIntroductionScenes;
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
    }
}
