package pub.pigeon.yggdyy.hexcreating.blocks.connector;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;
import pub.pigeon.yggdyy.hexcreating.api.iotawire.IotaWireProcessor;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;
import pub.pigeon.yggdyy.hexcreating.items.ModItems;
import pub.pigeon.yggdyy.hexcreating.libs.IotaWireConnectorPropertyHelper;
import pub.pigeon.yggdyy.hexcreating.libs.PlayerInventoryHelper;

public class IotaWireConnectorBlock extends Block implements IBE<IotaWireConnectorBlockEntity>, IWrenchable {
    public static BooleanProperty UP = BooleanProperty.of("up"), DOWN = BooleanProperty.of("down"), NORTH = BooleanProperty.of("north"), SOUTH = BooleanProperty.of("south"), WEST = BooleanProperty.of("west"), EAST = BooleanProperty.of("east");
    public static double H_WIDTH = 0.1, HEIGHT = 0.15;
    public static VoxelShape
            UP_BOX = VoxelShapes.cuboid(0.5 - H_WIDTH, 1.0 - HEIGHT, 0.5 - H_WIDTH, 0.5 + H_WIDTH, 1.0, 0.5 + H_WIDTH),
            DOWN_BOX = VoxelShapes.cuboid(0.5 - H_WIDTH, 0.0, 0.5 - H_WIDTH, 0.5 + H_WIDTH, HEIGHT, 0.5 + H_WIDTH),
            NORTH_BOX = VoxelShapes.cuboid(0.5 - H_WIDTH, 0.5 - H_WIDTH, 0.0, 0.5 + H_WIDTH, 0.5 + H_WIDTH, HEIGHT),
            SOUTH_BOX = VoxelShapes.cuboid(0.5 - H_WIDTH, 0.5 - H_WIDTH, 1.0 - HEIGHT, 0.5 + H_WIDTH, 0.5 + H_WIDTH, 1.0),
            WEST_BOX = VoxelShapes.cuboid(0.0, 0.5 - H_WIDTH, 0.5 - H_WIDTH, HEIGHT, 0.5 + H_WIDTH, 0.5 + H_WIDTH),
            EAST_BOX = VoxelShapes.cuboid(1.0 - HEIGHT, 0.5 - H_WIDTH, 0.5 - H_WIDTH, 1.0, 0.5 + H_WIDTH, 0.5 + H_WIDTH);

    public IotaWireConnectorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public Class<IotaWireConnectorBlockEntity> getBlockEntityClass() {
        return IotaWireConnectorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends IotaWireConnectorBlockEntity> getBlockEntityType() {
        return ModBlockEntities.IOTA_WIRE_CONNECTOR;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(UP, DOWN, NORTH, SOUTH, WEST, EAST);
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        VoxelShape ret = VoxelShapes.empty();
        if(blockState.get(UP)) ret = VoxelShapes.combineAndSimplify(ret, UP_BOX, BooleanBiFunction.OR);
        if(blockState.get(DOWN)) ret = VoxelShapes.combineAndSimplify(ret, DOWN_BOX, BooleanBiFunction.OR);
        if(blockState.get(NORTH)) ret = VoxelShapes.combineAndSimplify(ret, NORTH_BOX, BooleanBiFunction.OR);
        if(blockState.get(SOUTH)) ret = VoxelShapes.combineAndSimplify(ret, SOUTH_BOX, BooleanBiFunction.OR);
        if(blockState.get(WEST)) ret = VoxelShapes.combineAndSimplify(ret, WEST_BOX, BooleanBiFunction.OR);
        if(blockState.get(EAST)) ret = VoxelShapes.combineAndSimplify(ret, EAST_BOX, BooleanBiFunction.OR);
        return ret;
    }

    @Override
    public void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
        super.onBreak(world, blockPos, blockState, playerEntity);
        if(!world.isClient) {
            IotaWireConnectorBlockEntity be = (IotaWireConnectorBlockEntity) world.getBlockEntity(blockPos);
            if(be != null) {
                for(var c : be.getIotaWireConnectors()) {
                    c.disConnectAll();
                }
            }

            if(!(playerEntity == null || playerEntity.isCreative())) {
                int cnt = 0;
                for (var p : IotaWireConnectorPropertyHelper.getAllProperty()) {
                    if (blockState.get(p)) ++cnt;
                }
                var stack = new ItemStack(ModItems.IOTA_WIRE_CONNECTOR);
                stack.setCount(cnt);
                var pos = blockPos.toCenterPos();
                world.spawnEntity(new ItemEntity(world, pos.x, pos.y, pos.z, stack));
            }
        }
    }


    @Override
    public ActionResult onSneakWrenched(BlockState state, ItemUsageContext context) {
        if(!context.getWorld().isClient) {
            var rp = context.getHitPos().add(-context.getBlockPos().getX(), -context.getBlockPos().getY(), -context.getBlockPos().getZ());
            double eps = 0.005;
            var rpBox = VoxelShapes.cuboid(rp.x - eps, rp.y - eps, rp.z - eps, rp.x + eps, rp.y + eps, rp.z + eps);
            var pList = IotaWireConnectorPropertyHelper.getAllProperty();
            for (var property : pList) {
                var shape = IotaWireConnectorPropertyHelper.getVoxelBox(property);
                if (state.get(property) && shape.getBoundingBox().intersects(rpBox.getBoundingBox())) {
                    IotaWireProcessor.tryGetConnector((ServerWorld) context.getWorld(), context.getBlockPos(), IotaWireConnectorPropertyHelper.getTerminalIdByProperty(property)).disConnectAll();
                    //HexcreatingMain.LOGGER.info("ok");
                    context.getWorld().setBlockState(context.getBlockPos(), state.with(property, false));
                    if(context.getPlayer() != null && !context.getPlayer().isCreative()) PlayerInventoryHelper.giveOrDrop(context.getPlayer(), new ItemStack(ModItems.IOTA_WIRE_CONNECTOR));//context.getPlayer().giveItemStack(new ItemStack(ModItems.IOTA_WIRE_CONNECTOR));
                    boolean flag = false;
                    for(var _property : pList) {
                        flag = flag || state.with(property, false).get(_property);
                    }
                    if(!flag) context.getWorld().removeBlock(context.getBlockPos(), true);

                    context.getWorld().updateListeners(context.getBlockPos(), state, state.with(property, false), 3);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.PASS;
    }
}
