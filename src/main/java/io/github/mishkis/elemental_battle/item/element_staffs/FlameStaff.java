package io.github.mishkis.elemental_battle.item.element_staffs;

import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.flame.ConeOfFireSpell;
import io.github.mishkis.elemental_battle.spells.flame.FlameVortexSpell;
import io.github.mishkis.elemental_battle.spells.flame.FlamingDashSpell;
import io.github.mishkis.elemental_battle.spells.flame.WallOfFireSpell;
import org.jetbrains.annotations.Nullable;

public class FlameStaff extends MagicStaffItem {
    public FlameStaff() {
        super("flame_staff");
    }

    private final Spell useSpell = new ConeOfFireSpell();
    private final Spell shieldSpell = new WallOfFireSpell();
    private final Spell dashSpell = new FlamingDashSpell();
    private final Spell areaAttackSpell = new FlameVortexSpell();

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
        return null;
    }

    @Override
    public @Nullable Spell getUltimateSpell() {
        return null;
    }
}