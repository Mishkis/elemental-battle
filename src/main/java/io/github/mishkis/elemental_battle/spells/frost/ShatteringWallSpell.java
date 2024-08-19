package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.ShatteringWallEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ShatteringWallSpell extends Spell {
    @Override
    protected Elements getElement() {
        return Elements.FROST;
    }

    @Override
    protected Integer getCooldown() {
        return 200;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        ShatteringWallEntity shatteringWall = new ShatteringWallEntity(ElementalBattleEntities.SHATTERING_WALL, world);

        shatteringWall.setOwner(user);
        shatteringWall.setUptime(60);

        world.spawnEntity(shatteringWall);
    }
}
