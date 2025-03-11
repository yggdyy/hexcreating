package pub.pigeon.yggdyy.hexcreating.listeners;

import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import pub.pigeon.yggdyy.hexcreating.items.ModItems;

public class AfterDeathEventListener implements ServerLivingEntityEvents.AfterDeath {
    private final Random random = Random.create();
    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        Vec3d ePos = entity.getPos().add(new Vec3d(0, 0.2, 0));
        BlockPos pos = BlockPos.ofFloored(ePos);
        ServerWorld world = (ServerWorld) entity.getWorld();
        if(entity instanceof MobEntity mob && !IXplatAbstractions.INSTANCE.isBrainswept(mob) && world.getBlockState(pos).isOf(Blocks.SOUL_FIRE)) {
            int cnt = Math.abs(random.nextDouble()) < 0.25 ? 2 : 1;
            for(int i = 0; i < cnt; ++i) {
                ItemStack stack = new ItemStack(ModItems.RAW_SOUL);
                stack.setCount(1);
                ModItems.RAW_SOUL.setEntityId(stack, EntityType.getId(entity.getType()));
                ModItems.RAW_SOUL.setSoulAmount(stack, 25);
                ItemEntity drop = new ItemEntity(world, ePos.x, ePos.y, ePos.z, stack);
                world.spawnEntity(drop);
            }
        }
    }
}
