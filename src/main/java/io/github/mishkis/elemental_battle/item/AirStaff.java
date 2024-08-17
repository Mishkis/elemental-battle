package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.air_staff.GustEntity;
import io.github.mishkis.elemental_battle.item.helpers.MagicStaffItem;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AirStaff extends MagicStaffItem {
    public AirStaff() {
        super("air_staff", new Settings());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            GustEntity gust = new GustEntity(ElementalBattleEntities.GUST, world);

            gust.setOwner(user);

            gust.setVelocity(user.getRotationVector());
            gust.setPosition(user.getEyePos().add(user.getRotationVector()));

            gust.setNoGravity(true);

            if (user.getAttached(GustEntity.EMPOWERED_ATTACHMENT) != null) {
                if (user.shouldIgnoreFallDamageFromCurrentExplosion() && !user.isOnGround()) {
                    gust.setEmpowered();

                    // Summons an additional 2 gust entities.
                    for(int i = 0; i < 2; i++) {
                        // All of this code is reused, might be worthwhile to simplify it so no repeating, but can't be bothered rn
                        GustEntity surroundingGusts = new GustEntity(ElementalBattleEntities.GUST, world);

                        surroundingGusts.setOwner(user);

                        // Taken from cone of fire, maybe make a helper class.
                        Vec3d offset = new Vec3d(0.4, 0, 0).rotateZ((float) (Math.PI * i));

                        offset = offset.rotateX((float) Math.toRadians(-user.getPitch()));
                        offset = offset.rotateY((float) Math.toRadians(-user.getYaw()));

                        Vec3d velocity = user.getRotationVector().add(offset);

                        surroundingGusts.setVelocity(velocity);
                        surroundingGusts.setPosition(user.getEyePos().add(velocity));

                        surroundingGusts.setNoGravity(true);

                        surroundingGusts.setEmpowered();

                        world.spawnEntity(surroundingGusts);

                    }
                }

                user.removeAttached(GustEntity.EMPOWERED_ATTACHMENT);
            }

            world.spawnEntity(gust);



            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public TypedActionResult shield(World world, PlayerEntity user, Hand hand) {
        return null;
    }

    @Override
    public TypedActionResult dash(World world, PlayerEntity user, Hand hand) {
        return null;
    }

    @Override
    public TypedActionResult areaAttack(World world, PlayerEntity user, Hand hand) {
        return null;
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
