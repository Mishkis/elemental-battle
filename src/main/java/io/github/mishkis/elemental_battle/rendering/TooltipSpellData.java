package io.github.mishkis.elemental_battle.rendering;

import io.github.mishkis.elemental_battle.spells.Spell;
import net.minecraft.item.tooltip.TooltipData;

public record TooltipSpellData(Spell spell) implements TooltipData {
}
