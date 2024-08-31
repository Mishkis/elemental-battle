package io.github.mishkis.elemental_battle.spells;

import net.minecraft.entity.player.PlayerEntity;

// This is mainly used by the spellComponent display to know if to render empowered overlay
public interface EmpoweredSpell {
    boolean isEmpowered(PlayerEntity user);
}
