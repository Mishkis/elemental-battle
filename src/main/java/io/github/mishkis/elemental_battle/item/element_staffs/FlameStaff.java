package io.github.mishkis.elemental_battle.item.element_staffs;

import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import io.github.mishkis.elemental_battle.spells.flame.*;
import org.jetbrains.annotations.Nullable;

public class FlameStaff extends MagicStaffItem {
    public FlameStaff() {
        super("flame_staff");
    }

    private final Spell useSpell = new ConeOfFireSpell();

    @Override
    protected SpellElement getElement() {
        return SpellElement.FLAME;
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