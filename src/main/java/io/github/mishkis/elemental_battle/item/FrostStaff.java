package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.IcicleBallEntity;
import io.github.mishkis.elemental_battle.entity.frost_staff.IcicleEntity;
import io.github.mishkis.elemental_battle.entity.frost_staff.ShatteringWallEntity;
import io.github.mishkis.elemental_battle.item.helpers.MagicWandItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FrostStaff extends MagicWandItem {
    public FrostStaff() {
        super("frost_staff", new Item.Settings());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }
        user.getItemCooldownManager().set(this, 1);

        Vec3d spawnPos = user.getPos().add(user.getRotationVector().multiply(3)).offset(Direction.UP, 1.3);

        IcicleBallEntity icicleBall = new IcicleBallEntity(ElementalBattleEntities.ICICLE_BALL, world);
        icicleBall.setPosition(spawnPos);

        icicleBall.setOwner(user);
        icicleBall.setDamage(5);

        world.spawnEntity(icicleBall);

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public TypedActionResult shield(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            ShatteringWallEntity shatteringWall = new ShatteringWallEntity(ElementalBattleEntities.SHATTERING_WALL, world);

            shatteringWall.setOwner(user);
            shatteringWall.setUptime(100);

            world.spawnEntity(shatteringWall);

            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public TypedActionResult dash(World world, PlayerEntity user, Hand hand) {
        return null;
    }

    @Override
    public TypedActionResult areaAttack(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            int spawnCount = 8;
            for (int i = 0; i < spawnCount; i++) {
                IcicleEntity icicle = new IcicleEntity(ElementalBattleEntities.ICICLE, world);

                icicle.setOwner(user);

                int rotationSpeed = 3;
                float yOffset = 0.8F;
                float spawnDistance = 1.5F;

                if (i >= spawnCount/2) {
                    rotationSpeed *= 2;
                    spawnDistance *= 2;
                }

                Vec3d spawnNormal = user.getRotationVector().multiply(1, 0, 1).normalize();
                spawnNormal = spawnNormal.rotateY((float) (4 * i * Math.PI / spawnCount)).multiply(spawnDistance);

                Vec3d spawnPos = user.getPos().add(spawnNormal).offset(Direction.UP, yOffset);
                icicle.setPosition(spawnPos);

                icicle.setOrbit(user, rotationSpeed, yOffset);
                icicle.setNoGravity(true);

                icicle.setVelocity(0.01, 0, 0); // for visual rotation on first tick spawned

                icicle.setDamage(3);

                world.spawnEntity(icicle);
            }
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public TypedActionResult special(World world, PlayerEntity user, Hand hand) {
        return null;
    }

    @Override
    public TypedActionResult ultimate(World world, PlayerEntity user, Hand hand) {
        return null;
    }
}
