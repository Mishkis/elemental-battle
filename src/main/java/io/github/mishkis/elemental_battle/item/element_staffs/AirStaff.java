package io.github.mishkis.elemental_battle.item.element_staffs;

import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import io.github.mishkis.elemental_battle.spells.air.*;
import org.jetbrains.annotations.Nullable;

public class AirStaff extends MagicStaffItem {
    public AirStaff() {
        super("air_staff");
    }

    private final Spell useSpell = new GustSpell();

    @Override
    public SpellElement getElement() {
        return SpellElement.AIR;
    }

    @Override
    public @Nullable Spell useSpell() {
        return useSpell;
    }

    @Override
    public @Nullable Spell ultimateSpell() {
        return null;
    }
}
