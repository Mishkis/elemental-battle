package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.flame_staff.ConeOfFireEntity;
import io.github.mishkis.elemental_battle.item.helpers.IdleAnimatedItem;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class FlameStaff extends IdleAnimatedItem {
    Random random = Random.create();

    public FlameStaff() {
        super("flame_staff", new Settings());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            Vec3d spawnPos = user.getPos().offset(Direction.UP, 1.5);

            int spawnCount = 8 + random.nextBetween(-1, 1);

            float randomSpread = random.nextBetween(-1, 1) * 0.1F;
            for (int i = 0; i < spawnCount; i++) {
                ConeOfFireEntity coneOfFire = new ConeOfFireEntity(ElementalBattleEntities.CONE_OF_FIRE, world);
                coneOfFire.setPosition(spawnPos);

                coneOfFire.setOwner(user);
                coneOfFire.setDamage(5);
                coneOfFire.setExpireTime(15);

                coneOfFire.setGravity(Vec3d.ZERO);

                Vec3d offset = new Vec3d(0.3 + randomSpread, 0, 0).rotateZ((float) (2 * Math.PI * i / spawnCount));

                offset = offset.rotateX((float) Math.toRadians(-user.getPitch()));
                offset = offset.rotateY((float) Math.toRadians(-user.getYaw()));

                Vec3d velocity = user.getRotationVector().add(offset);

                velocity = velocity.normalize();
                velocity = velocity.multiply(0.5);
                coneOfFire.setVelocity(velocity);

                world.spawnEntity(coneOfFire);
            }

            return TypedActionResult.success(user.getStackInHand(hand));
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}