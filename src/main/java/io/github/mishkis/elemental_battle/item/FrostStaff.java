package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.SpikeBallEntity;
import io.github.mishkis.elemental_battle.item.helpers.IdleAnimatedItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
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
        user.getItemCooldownManager().set(this, 1);

        Vec3d spawnPos = user.getPos().add(user.getRotationVector().multiply(3)).offset(Direction.UP, 1.3);

        SpikeBallEntity spikeBall = new SpikeBallEntity(ElementalBattleEntities.SPIKEBALL, world);
        spikeBall.setPosition(spawnPos);
        world.spawnEntity(spikeBall);

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
