package pub.pigeon.yggdyy.hexcreating.blocks.board;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.common.lib.HexSounds;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import kotlin.Triple;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;
import pub.pigeon.yggdyy.hexcreating.items.ModItems;
import pub.pigeon.yggdyy.hexcreating.libs.PlayerInventoryHelper;

import java.util.ArrayList;
import java.util.List;

public class BoardBlock extends Block implements IBE<BoardBlockEntity>, IWrenchable {
    public BoardBlock(Settings settings) {
        super(settings.luminance(state -> 15));
    }
    @Override
    public Class<BoardBlockEntity> getBlockEntityClass() {
        return BoardBlockEntity.class;
    }
    @Override
    public BlockEntityType<? extends BoardBlockEntity> getBlockEntityType() {
        return ModBlockEntities.BOARD;
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.HORIZONTAL_FACING);
    }
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
        return super.getPlacementState(itemPlacementContext).with(Properties.HORIZONTAL_FACING, itemPlacementContext.getHorizontalPlayerFacing().getOpposite());
    }

    private static final int SIMPLE_PUT = 1, SIMPLE_REMOVE = 2;
    private static final int SECTION_RIGHT = 4, SECTION_LEFT = 8, SECTION_UP = 16, SECTION_DOWN = 32; // 16 and 32 are now not used
    private static final int SECTION_COPY = 64, SECTION_PASTE = 128, SECTION_REMOVE = 256;
    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
        if(world.isClient) {
            world.playSoundAtBlockCenter(blockPos, HexSounds.ADD_TO_PATTERN, SoundCategory.BLOCKS, 1f, 1f, true);
            return ActionResult.SUCCESS;
        }
        if(world.getBlockEntity(blockPos) instanceof BoardBlockEntity be) {
            var s = playerEntity.getEyePos();
            float pitch = playerEntity.getPitch(), yaw = playerEntity.getYaw();
            Vec3d n = Vec3d.fromPolar(pitch, yaw);
            int slot = getSlot(blockState, blockPos, s, n);
            BoardConnected connected = new BoardConnected(world, blockPos, blockState);
            int condition = connected.condition;
            var pStack = playerEntity.getStackInHand(hand);
            var bStack = be.getStack(slot);
            /*int  = 0;
             = switch (condition) {
                case 0 -> SIMPLE_PUT | SIMPLE_REMOVE | SECTION_COPY | SECTION_PASTE; // default
                case 1 -> SIMPLE_PUT | SECTION_REMOVE | SECTION_COPY | SECTION_PASTE; // bulk delete
                case 2 -> SIMPLE_PUT | SECTION_LEFT | SECTION_COPY | SECTION_PASTE; // move left
                case 3 -> SIMPLE_PUT | SECTION_RIGHT | SECTION_COPY | SECTION_PASTE; // move right
                case 4 -> SIMPLE_PUT | SECTION_UP | SECTION_COPY | SECTION_PASTE;
                case 5 -> SIMPLE_PUT | SECTION_DOWN | SECTION_COPY | SECTION_PASTE;
                case 15 -> SECTION_COPY; // static
                default -> SECTION_COPY;
            };*/
            for(var operation : AllBoardOperations.operations) {
                if(operation.operate((ServerWorld) world, blockPos, blockState, be, slot, bStack, connected, condition, playerEntity, pStack)) break;
            }
        }
        return ActionResult.PASS;
    }
    public int getSlot(BlockState state, BlockPos pos, Vec3d s, Vec3d n) {
        var alpha = surface(state, pos);
        Vec3d hit = hitPos(state, pos, s, n);
        Vec3d o = alpha.getFirst(), x = alpha.getSecond(), y = alpha.getThird();
        Vec3d o2hit = hit.add(o.multiply(-1));
        double kx = o2hit.dotProduct(x) / (x.length() * x.length()) * 4.0,
                ky = o2hit.dotProduct(y) / (y.length() * y.length()) * 4.0;
        int r = (int) Math.floor(ky), c = (int) Math.floor(kx);
        return MathHelper.clamp(r * 4 + c, 0, 15);
    }
    public int getCondition(BlockState state, World world, BlockPos pos) {
        return new BoardConnected(world, pos, state).condition;
    }
    public Vec3d hitPos(BlockState state, BlockPos pos, Vec3d s, Vec3d n) {
        Triple<Vec3d, Vec3d, Vec3d> alpha = surface(state, pos);
        Vec3d o = alpha.getFirst(), x = alpha.getSecond(), y = alpha.getThird();
        Vec3d f = x.crossProduct(y);
        Vec3d o2s = s.add(o.multiply(-1));
        double k = -o2s.dotProduct(f) / n.dotProduct(f);
        return s.add(n.multiply(k));
    }
    //Triple<origin, x, y>
    public Triple<Vec3d, Vec3d, Vec3d> surface(BlockState state, BlockPos pos) {
       Vec3d c = pos.toCenterPos();
       return switch (state.get(Properties.HORIZONTAL_FACING)) {
           case NORTH -> new Triple<>(c.add(new Vec3d(0.5, 0.5, 0.25)), new Vec3d(-1, 0, 0), new Vec3d(0, -1, 0));
           case SOUTH -> new Triple<>(c.add(new Vec3d(-0.5, 0.5, -0.25)), new Vec3d(1, 0, 0), new Vec3d(0, -1, 0));
           case WEST -> new Triple<>(c.add(new Vec3d(0.25, 0.5, -0.5)), new Vec3d(0, 0, 1), new Vec3d(0, -1, 0));
           case EAST -> new Triple<>(c.add(-0.25, 0.5, 0.5), new Vec3d(0, 0, -1), new Vec3d(0, -1, 0));
           default -> null;
       };
    }
    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        return switch (blockState.get(Properties.HORIZONTAL_FACING)) {
            case NORTH -> VoxelShapes.cuboid(0, 0, 0.75, 1, 1, 1);
            case SOUTH -> VoxelShapes.cuboid(0, 0, 0, 1, 1, 0.25);
            case WEST -> VoxelShapes.cuboid(0.75, 0, 0, 1, 1, 1);
            case EAST -> VoxelShapes.cuboid(0, 0, 0, 0.25, 1, 1);
            default -> VoxelShapes.fullCube();
        };
    }
    @Override
    public void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
        super.onBreak(world, blockPos, blockState, playerEntity);
        if(!world.isClient && world.getBlockEntity(blockPos) instanceof BoardBlockEntity be) {
            for(int i = 0; i < 16; ++i) {
                var stack = be.getStack(i);
                if(!stack.isEmpty()) {
                    Vec3d p = blockPos.toCenterPos();
                    ItemEntity entity = new ItemEntity(world, p.x, p.y, p.z, stack.copy());
                    world.spawnEntity(entity);
                }
            }
        }
    }
    @Override
    public ActionResult onSneakWrenched(BlockState state, ItemUsageContext context) {
        return ActionResult.PASS;
    }
}
