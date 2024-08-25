package io.github.mishkis.elemental_battle.item.element_staffs;

import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.air.*;
import org.jetbrains.annotations.Nullable;

public class AirStaff extends MagicStaffItem {
    public AirStaff() {
        super("air_staff");
    }

    private final Spell useSpell = new GustSpell();
    private final Spell shieldSpell = new BlowBackSpell();
    private final Spell dashSpell = new DoubleDashSpell();
    private final Spell areaAttackSpell = new AirLiftSpell();
    private final Spell specialSpell = new SlamDownSpell();

    @Override
    public @Nullable Spell getUseSpell() {
        return useSpell;
    }

    @Override
    public @Nullable Spell getShieldSpell() {
        return shieldSpell;
    }

    @Override
    public @Nullable Spell getDashSpell() {
        return dashSpell;
    }

    @Override
    public @Nullable Spell getAreaAttackSpell() {
        return areaAttackSpell;
    }

    @Override
    public @Nullable Spell getSpecialSpell() {
        return specialSpell;
    }

    @Override
    public @Nullable Spell getUltimateSpell() {
        return null;
    }
}
