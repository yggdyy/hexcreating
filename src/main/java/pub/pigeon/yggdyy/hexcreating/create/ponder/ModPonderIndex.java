package pub.pigeon.yggdyy.hexcreating.create.ponder;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;
import pub.pigeon.yggdyy.hexcreating.items.ModItems;

public class ModPonderIndex {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(HexcreatingMain.MOD_ID);
    public static void init() {
        HELPER.forComponents(ModItems.THOUGHT_KEY_HEXCASTING_INTRODUCTION).
                addStoryBoard("hex/introduction", HexIntroductionScenes::hexcasting);
    }
}
