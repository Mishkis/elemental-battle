package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.IcicleBallEntity;
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
