package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.FrozenSlideEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FrozenSlideSpell extends Spell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "frozen_slide");
    }

    @Override
    protected SpellElement getElement() {
        return SpellElement.FROST;
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
