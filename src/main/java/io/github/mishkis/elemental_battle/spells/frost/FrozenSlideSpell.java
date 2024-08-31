package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.FrozenSlideEntity;
import io.github.mishkis.elemental_battle.spells.EmpoweredSpell;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FrozenSlideSpell extends Spell implements EmpoweredSpell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "frozen_slide");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.FROST;
    }

    @Override
    public int getCooldown() {
        return 100;
    }

    @Override
    public String getDescription() {
        return "The floor beneath you is transformed to ice while you are surrounded by a protective barrier, allowing you to travel forward. If used after a successful parry, the spell is empowered.";
    }

    @Override
    public float getDamage() {
        return 5;
    }

    @Override
    public int getUptime() {
        return 20;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        world.spawnEntity(genericEntity(user, new FrozenSlideEntity(ElementalBattleEntities.FROZEN_SLIDE, world)));
    }

    @Override
    public boolean isEmpowered(PlayerEntity user) {
        return user.getStatusEffect(ElementalBattleStatusEffects.SUCCESSFUL_PARRY_EFFECT) != null;
    }
}
