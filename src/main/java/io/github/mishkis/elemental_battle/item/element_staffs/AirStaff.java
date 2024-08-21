package io.github.mishkis.elemental_battle.item.element_staffs;

import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.air.GustSpell;
import org.jetbrains.annotations.Nullable;

public class AirStaff extends MagicStaffItem {
    public AirStaff() {
        super("air_staff");
    }

    private Spell useSpell = new GustSpell();

    @Override
    public @Nullable Spell getUseSpell() {
        return useSpell;
    }

    @Override
    public @Nullable Spell getShieldSpell() {
        return null;
    }

    @Override
    public @Nullable Spell getDashSpell() {
        return null;
    }

    @Override
    public @Nullable Spell getAreaAttackSpell() {
        return null;
    }

    @Override
    public @Nullable Spell getSpecialSpell() {
        return null;
    }

    @Override
    public @Nullable Spell getUltimateSpell() {
        return null;
    }
}
