package io.github.mishkis.elemental_battle.status_effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SpellLockEffect extends StatusEffect {
    protected SpellLockEffect() {
        super(StatusEffectCategory.HARMFUL, 0xac3232);
    }
}
