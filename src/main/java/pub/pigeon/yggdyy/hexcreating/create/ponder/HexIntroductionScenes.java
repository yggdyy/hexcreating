package pub.pigeon.yggdyy.hexcreating.create.ponder;

import at.petrak.hexcasting.common.lib.HexItems;
import com.simibubi.create.foundation.ponder.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class HexIntroductionScenes {
    public static void hexcasting(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("hexcasting_introduction", "The Overall Introduction to Hexcasting");
        scene.configureBasePlate(0, 0, 5);

        scene.world.showSection(util.select.layer(0), Direction.UP);
        scene.world.showSection(util.select.layer(1), Direction.DOWN);

        BlockPos staffBase = util.grid.at(2, 1, 2),
                amethystBase = util.grid.at(1, 1, 1),
                lensBase = util.grid.at(1, 1, 3),
                scrollBase = util.grid.at(3, 1, 3),
                focusBase = util.grid.at(3, 1, 1);
        ItemStack staff = new ItemStack(HexItems.STAFF_EDIFIED),
                amethyst = new ItemStack(HexItems.CHARGED_AMETHYST),
                lens = new ItemStack(HexItems.SCRYING_LENS),
                scroll = new ItemStack(HexItems.SCROLL_LARGE),
                focus = new ItemStack(HexItems.FOCUS);

        scene.world.createItemEntity(amethystBase.up().toCenterPos(), Vec3d.ZERO, amethyst);
        scene.overlay.showText(100)
                .attachKeyFrame()
                .text("Hexcasting is a magical mod where you are allowed to use amethyst as media, ")
                .pointAt(util.vector.blockSurface(amethystBase, Direction.UP))
                .placeNearTarget();
        scene.idle(110);

        scene.world.createItemEntity(staffBase.up().toCenterPos(), Vec3d.ZERO, staff);
        scene.overlay.showText(40)
                .attachKeyFrame()
                .text("and use a staff to...")
                .pointAt(util.vector.blockSurface(staffBase, Direction.UP))
                .placeNearTarget();
        scene.idle(50);

        scene.world.createItemEntity(scrollBase.up().toCenterPos(), Vec3d.ZERO, scroll);
        scene.world.createItemEntity(lensBase.up().toCenterPos(), Vec3d.ZERO, lens);
        scene.overlay.showText(60)
                .attachKeyFrame()
                .text("draw many patterns on a hexagonal dots field, ")
                .pointAt(util.vector.blockSurface(lensBase, Direction.UP))
                .placeNearTarget();
        scene.idle(70);

        scene.world.createItemEntity(focusBase.up().toCenterPos(), Vec3d.ZERO, focus);
        scene.overlay.showText(110)
                .attachKeyFrame()
                .text("to get, calculate and use the information (Iotas) in your information storage (Stack) to cast powerful spells.")
                .pointAt(util.vector.blockSurface(focusBase, Direction.UP))
                .placeNearTarget();
        scene.idle(120);
    }
}
