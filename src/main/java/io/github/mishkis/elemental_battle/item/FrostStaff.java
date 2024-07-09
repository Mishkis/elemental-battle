package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.item.helpers.IdleAnimatedItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FrostStaff extends IdleAnimatedItem {
    public FrostStaff() {
        super("frost_staff", new Item.Settings());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }
        user.getItemCooldownManager().set(this, 100);

        ElementalBattle.LOGGER.info("Using...");

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
