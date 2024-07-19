package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.flame_staff.ConeOfFireEntity;
import io.github.mishkis.elemental_battle.item.helpers.IdleAnimatedItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FlameStaff extends IdleAnimatedItem {
    public FlameStaff() {
        super("flame_staff", new Settings());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && user instanceof PlayerEntity) {
            Vec3d spawnPos = user.getPos().offset(Direction.UP, 1.3);

            int spawnCount = 8;
            for (int i = 0; i < spawnCount; i++) {
                ConeOfFireEntity coneOfFire = new ConeOfFireEntity(ElementalBattleEntities.CONE_OF_FIRE, world);
                coneOfFire.setPosition(spawnPos);

                coneOfFire.setOwner(user);
                coneOfFire.setDamage(3);
                coneOfFire.setExpireTime(30);

                coneOfFire.setGravity(Vec3d.ZERO);

                Vec3d offset = new Vec3d(30, 0, 0).rotateY((float) (2 * Math.PI * i / spawnCount));

                Vec3d velocity = user.getRotationVector((float) (user.getPitch() + offset.x), (float) (user.getYaw() + offset.z));

                velocity = velocity.multiply(0.5);
                coneOfFire.setVelocity(velocity);

                world.spawnEntity(coneOfFire);
            }

            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}