package pub.pigeon.yggdyy.hexcreating.create.ponder.scenees;

import at.petrak.hexcasting.api.casting.iota.BooleanIota;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import at.petrak.hexcasting.common.lib.HexItems;
import com.simibubi.create.foundation.ponder.*;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import pub.pigeon.yggdyy.hexcreating.create.ponder.HexPonderUtils;

public class HexIntroductionScenes {
    public static int A_LONG_TIME = 20 * 60 * 10;
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
    public static void basicIotaEntity(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("basic_iota_entity", "Basic Iotas - Entity");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.layer(0), Direction.UP);
        BlockPos iotaPos = util.grid.at(4, 1, 4);

        scene.overlay.showText(60).text("The information needed to cast spells in HexCasting is called iota.").attachKeyFrame();
        scene.idle(65);
        scene.overlay.showText(80).text("Iotas have many types, and the most basic four are Entity Iota, Number Iota, Vector Iota and Boolean Iota").attachKeyFrame();
        scene.idle(80);

        Vec3d boatPos = util.grid.at(0, 1, 0).toCenterPos(),
                villagerPos = util.grid.at(0, 1, 3).toCenterPos(),
                itemPos = util.grid.at(3, 1, 0).toCenterPos();
        scene.world.createEntity(world -> {
            BoatEntity boat = new BoatEntity(EntityType.BOAT, world);
            boat.setPosition(boatPos);
            return boat;
        });
        scene.world.createEntity(world -> {
            VillagerEntity village = new VillagerEntity(EntityType.VILLAGER, world);
            village.setPosition(villagerPos);
            return village;
        });
        scene.world.createItemEntity(itemPos, Vec3d.ZERO, new ItemStack(Items.AMETHYST_BLOCK));
        scene.idle(20);

        scene.overlay.showText(60).text("The iota of entity represents a entity in the world.");
        scene.idle(65);
        scene.overlay.showText(60).text("You need this type iota to show what is a spell's target.").attachKeyFrame();
        scene.idle(65);
        scene.overlay.showText(60).pointAt(boatPos).text("It can be a boat, a minecraft, or other entities like them").attachKeyFrame();
        scene.idle(65);
        scene.overlay.showText(60).pointAt(villagerPos).text("also living entities like villagers and zombies").attachKeyFrame();
        scene.idle(65);
        scene.overlay.showText(60).pointAt(itemPos).text("Since item drops are also entities, Entity Iota can represent a drop of item").attachKeyFrame();
        scene.idle(60);
    }
    public static void basicIotaNumber(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("basic_iota_number", "Basic Iotas - Number");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.layer(0), Direction.UP);
        BlockPos iotaPos = util.grid.at(4, 1, 4);

        scene.overlay.showText(60).text("Number Iota represents a number (precisely a float number)").attachKeyFrame();
        scene.idle(65);
        scene.overlay.showText(130).text("For example...").attachKeyFrame();
        scene.idle(10);

        HexPonderUtils.IotaDisplay
                iotaDisplay1 = new HexPonderUtils.IotaDisplay(scene, util, new DoubleIota(1)),
                iotaDisplay2 = new HexPonderUtils.IotaDisplay(scene, util, new DoubleIota(3.14)),
                iotaDisplay3 = new HexPonderUtils.IotaDisplay(scene, util, new DoubleIota(-2.71)),
                iotaDisplay4 = new HexPonderUtils.IotaDisplay(scene, util, new DoubleIota(114514));
        iotaDisplay1.setPos(iotaPos); iotaDisplay2.setPos(iotaPos.up()); iotaDisplay3.setPos(iotaPos.west(2)); iotaDisplay4.setPos(iotaPos.west(4));
        iotaDisplay1.showIota(120);
        scene.idle(30);
        iotaDisplay2.showIota(90);
        scene.idle(30);
        iotaDisplay3.showIota(60);
        scene.idle(30);
        iotaDisplay4.showIota(30);
        scene.idle(30);
    }
    public static void basicIotaVector(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("basic_iota_vector", "Basic Iota - Vector");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.layer(0), Direction.UP);
        BlockPos iotaPos = util.grid.at(2, 1, 2);
        HexPonderUtils.IotaDisplay iotaDisplay = new HexPonderUtils.IotaDisplay(scene, util, new Vec3Iota(new Vec3d(11, 45, 14)));
        iotaDisplay.setPos(iotaPos);

        scene.overlay.showText(60).text("Vector Iota represents a 3D vector.").attachKeyFrame();
        scene.idle(65);
        scene.overlay.showText(60).text("It is usually shown in the form of (x, y, z)").attachKeyFrame();
        iotaDisplay.showIota(190);
        scene.idle(65);
        scene.overlay.showText(60).text("It is used to declare the position or direction (and so on) needed for spells.").attachKeyFrame();
        scene.idle(65);
        scene.overlay.showText(60).text("If you don't know what is a vector, the Thought Key - Vector may help you").attachKeyFrame();
        scene.idle(60);
    }
    public static void baseIotaBool(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("basic_iota_bool", "Basic Iota - Boolean");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.layer(0), Direction.UP);
        BlockPos iotaPos = util.grid.at(1, 1, 2);
        HexPonderUtils.IotaDisplay iotaDisplay1 = new HexPonderUtils.IotaDisplay(scene, util, new BooleanIota(false)),
                iotaDisplay2 = new HexPonderUtils.IotaDisplay(scene, util, new BooleanIota(true));
        iotaDisplay1.setPos(iotaPos); iotaDisplay2.setPos(iotaPos);
        BlockPos lightPos = util.grid.at(3, 1, 2);

        scene.overlay.showText(60).text("Boolean Iota represents a state of True or False").attachKeyFrame();
        scene.idle(65);
        scene.overlay.showText(60).text("It is used to presents whether a conclusion is right or wrong").attachKeyFrame();
        scene.idle(65);

        scene.world.setBlock(lightPos, Blocks.REDSTONE_LAMP.getDefaultState().with(RedstoneLampBlock.LIT, false), false);
        scene.world.showSection(util.select.position(lightPos), Direction.DOWN);
        scene.overlay.showText(60).text("Now the conclution of [This lamp is on] is wrong (False)").attachKeyFrame().pointAt(util.vector.blockSurface(lightPos, Direction.UP)).placeNearTarget();
        iotaDisplay1.showIota(60);
        scene.idle(65);
        iotaDisplay1.hideIota();

        scene.world.setBlock(lightPos, Blocks.REDSTONE_LAMP.getDefaultState().with(RedstoneLampBlock.LIT, true), false);
        scene.overlay.showText(60).text("But now the conclution of [This lamp is on] is right (True)").attachKeyFrame().pointAt(util.vector.blockSurface(lightPos, Direction.UP)).placeNearTarget();
        iotaDisplay2.showIota(60);
        scene.idle(60);
    }
    public static void vector(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("vector", "The most important iota - Vector");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.layer(0), Direction.UP);

        scene.overlay.showText(50).text("You might don't know what is vector").attachKeyFrame();
        scene.idle(55);
        scene.overlay.showText(70).text("But you must know we can use a coordinate (x, y, z) to show a position in the world");
        scene.idle(75);

        HexPonderUtils.ArrowDisplay xAxis = new HexPonderUtils.ArrowDisplay(scene, util), yAxis = new HexPonderUtils.ArrowDisplay(scene, util), zAxis = new HexPonderUtils.ArrowDisplay(scene, util);
        xAxis.start(new Vec3d(0.1, 0, 0)).end(new Vec3d(6, 0, 0)).color(PonderPalette.RED, PonderPalette.RED).size(0.5);
        yAxis.start(new Vec3d(0, 0.1, 0)).end(new Vec3d(0, 6, 0)).color(PonderPalette.GREEN, PonderPalette.GREEN).size(0.5);
        zAxis.start(new Vec3d(0, 0, 0.1)).end(new Vec3d(0, 0, 6)).color(PonderPalette.BLUE, PonderPalette.BLUE).size(0.5);
        scene.overlay.showText(40).attachKeyFrame().text("This is origin point").pointAt(new Vec3d(0, 0, 0)).placeNearTarget();
        scene.idle(45);
        xAxis.show(A_LONG_TIME);
        scene.overlay.showText(40).text("This is x axis").pointAt(new Vec3d(6, 0, 0)).placeNearTarget();
        scene.idle(45);
        yAxis.show(A_LONG_TIME);
        scene.overlay.showText(40).text("This is y axis").pointAt(new Vec3d(0, 6, 0)).placeNearTarget();
        scene.idle(45);
        zAxis.show(A_LONG_TIME);
        scene.overlay.showText(40).text("This is z axis").pointAt(new Vec3d(0, 0, 6)).placeNearTarget();
        scene.idle(50);

        scene.overlay.showText(50).attachKeyFrame().text("So now we can represent point by the axis");
        scene.idle(55);
        scene.overlay.showText(360).text("For example, point (2, 3, 1)...");
        scene.idle(40);
        scene.overlay.showText(50).attachKeyFrame().text("We start from the origin point...").pointAt(Vec3d.ZERO).placeNearTarget();
        scene.idle(55);
        scene.overlay.showText(50).text("then move in the direction of x Axis for 2 blocks...").pointAt(new Vec3d(2, 0, 0)).placeNearTarget();
        scene.idle(55);
        scene.overlay.showText(50).text("next move in the direction of y Axis for 3 blocks...").pointAt(new Vec3d(2, 3, 0)).placeNearTarget();
        scene.overlay.showLine(PonderPalette.GREEN, new Vec3d(2, 0, 0), new Vec3d(2, 3, 0), 110);
        scene.idle(55);
        scene.overlay.showText(50).text("finally move in the direction of z Axis for 1 block...").pointAt(new Vec3d(2, 3, 1)).placeNearTarget();
        scene.overlay.showLine(PonderPalette.BLUE, new Vec3d(2, 3, 0), new Vec3d(2, 3, 1), 55);
        scene.idle(55);
        scene.overlay.showText(60).attachKeyFrame().text("This is the point (2, 3, 1)").pointAt(new Vec3d(2, 3, 1));
        scene.idle(65);

        scene.overlay.showText(140).attachKeyFrame().text("So for this point (2, 3, 1)...").pointAt(new Vec3d(2, 3, 1));
        scene.idle(45);
        scene.overlay.showText(95).text("from the origin point...").pointAt(Vec3d.ZERO).placeNearTarget();
        scene.idle(20);
        scene.world.hideSection(util.select.layer(0), Direction.DOWN);
        scene.idle(25);
        HexPonderUtils.ArrowDisplay vector1 = new HexPonderUtils.ArrowDisplay(scene, util);
        vector1.size(0.25).start(new Vec3d(0.05, 0.05, 0.05)).end(new Vec3d(2, 3, 1)).color(PonderPalette.SLOW, PonderPalette.SLOW);
        vector1.show(A_LONG_TIME);
        scene.overlay.showText(50).text("we make an arrow to it").pointAt(new Vec3d(1, 1.5, 0.5)).placeNearTarget();
        scene.idle(55);
        scene.overlay.showText(180).attachKeyFrame().text("This arrow is the vector (2, 3, 1)").pointAt(new Vec3d(2, 3, 1));
        scene.idle(30);
        scene.world.showSection(util.select.layer(0), Direction.UP);
        scene.idle(35);
        scene.overlay.showText(60).attachKeyFrame().text("Obviously, the vector (2, 3, 1) can represents the position of point (2, 3, 1)");
        scene.idle(65);
        scene.overlay.showText(50).text("as well as the direction of this arrow");
        scene.idle(55);
        scene.overlay.showText(60).text("vector (2, 3, 1) can also show [move from point (0, 0, 0) to (2, 3, 1)]");
        scene.idle(65);
        scene.overlay.showText(60).attachKeyFrame().text("the arrow's length is called the vector's length or mode").pointAt(new Vec3d(2, 3, 1)).placeNearTarget();
        scene.idle(65);
        scene.overlay.showText(60).text("as for vector (2, 3, 1), its length is about 3.74 (sqrt(14))").pointAt(new Vec3d(2, 3, 1)).placeNearTarget();
        scene.idle(65);

        HexPonderUtils.ArrowDisplay vector2 = new HexPonderUtils.ArrowDisplay(scene, util);
        vector2.size(0.25).start(new Vec3d(0, -0.05, 0.05)).end(new Vec3d(0, -0.5, 2)).color(PonderPalette.FAST, PonderPalette.FAST);
        vector2.show(A_LONG_TIME);
        scene.overlay.showText(60).attachKeyFrame().text("Through the same way, we can get the vector (0, -0.5, 2)").pointAt(new Vec3d(0, -1, 0)).placeNearTarget();
        scene.idle(65);
        scene.overlay.showText(A_LONG_TIME).attachKeyFrame().text("Just like numbers, vectors can be calculated");
        scene.idle(40);
        scene.overlay.showText(60).attachKeyFrame().text("A vector can add up another vector with the result of a new vector").pointAt(Vec3d.ZERO);
        scene.idle(65);
        scene.overlay.showText(60).text("(a, b, c) + (x, y, z) = (a + x, b + y, c + z)").pointAt(Vec3d.ZERO);
        scene.idle(65);
        scene.overlay.showText(45).attachKeyFrame().text("For example, to calculate (2, 3, 1) + (0, -0.5, 2)").pointAt(Vec3d.ZERO);
        scene.idle(50);
        HexPonderUtils.ArrowDisplay vector2_1 = new HexPonderUtils.ArrowDisplay(scene, util);
        vector2_1.size(0.25).start(new Vec3d(2, 3, 1.05)).end(new Vec3d(2, 2.5, 3)).color(PonderPalette.FAST, PonderPalette.FAST);
        HexPonderUtils.ArrowDisplay vector3 = new HexPonderUtils.ArrowDisplay(scene, util);
        vector3.size(0.25).start(new Vec3d(0, 0, 0)).end(new Vec3d(2, 2.5, 3)).color(PonderPalette.MEDIUM, PonderPalette.MEDIUM);
        vector2_1.show(A_LONG_TIME);
        scene.overlay.showText(60).text("We put the tail of (0, -0.5, 2) to the head of (2, 3, 1)").pointAt(new Vec3d(2, 2.75, 2)).placeNearTarget();
        scene.idle(30);
        scene.world.hideSection(util.select.layer(0), Direction.DOWN);
        scene.idle(35);
        vector3.show(A_LONG_TIME);
        scene.overlay.showText(100).text("Then we get the result (2, 3, 1) + (0, -0.5, 2) = (2 + 0, 3 - 0.5, 1 + 2) = (2, 2.5, 3)").pointAt(new Vec3d(2, 2.5, 3)).placeNearTarget();
        scene.idle(20);
        scene.rotateCameraY(-50);
        scene.idle(35);
        scene.world.showSection(util.select.layer(0), Direction.UP);
        scene.idle(30);
        scene.rotateCameraY(50);
        scene.idle(30);
        scene.overlay.showText(60).attachKeyFrame().text("A vector can also multiply with a number").pointAt(Vec3d.ZERO);
        scene.idle(65);
        scene.overlay.showText(60).text("the result is a new vector with the same direction and multiplied length as the old one.").pointAt(Vec3d.ZERO);
        scene.idle(65);
        scene.overlay.showText(60).text("that is (x, y, z) * k = (x * k, y * k, z * k)").pointAt(Vec3d.ZERO);
        scene.idle(35);
        scene.world.hideSection(util.select.layer(0), Direction.DOWN);
        scene.idle(30);
        HexPonderUtils.ArrowDisplay vector4 = new HexPonderUtils.ArrowDisplay(scene, util);
        vector4.size(0.25).start(new Vec3d(0.05, 0, 0)).end(new Vec3d(1, 1.5, 0.5)).color(PonderPalette.OUTPUT, PonderPalette.OUTPUT);
        vector4.show(A_LONG_TIME);
        scene.overlay.showText(100).text("For example, this vector (1, 1.5, 0.5), is the result of (2, 3, 1) * 0.5").pointAt(new Vec3d(1, 1.5, 0.5)).placeNearTarget();
        scene.idle(105);
    }
}
