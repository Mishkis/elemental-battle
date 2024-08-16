package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.air_staff.GustEntity;
import io.github.mishkis.elemental_battle.item.helpers.MagicStaffItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
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
