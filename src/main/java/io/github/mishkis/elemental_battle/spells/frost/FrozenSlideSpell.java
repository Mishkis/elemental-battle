package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.FrozenSlideEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class FrozenSlideSpell extends Spell {
    @Override
    protected Elements getElement() {
        return Elements.FROST;
    }

    @Override
    protected Integer getCooldown() {
        return 100;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        FrozenSlideEntity frozenSlide = new FrozenSlideEntity(ElementalBattleEntities.FROZEN_SLIDE, world);

        frozenSlide.setOwner(user);
        frozenSlide.setPosition(user.getPos());

        world.spawnEntity(frozenSlide);
    }
}
