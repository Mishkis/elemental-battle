package io.github.mishkis.elemental_battle.item.element_staffs;

import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.frost.*;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

public class FrostStaff extends MagicStaffItem {
    public FrostStaff() {
        super("frost_staff");
    }

    private Spell useSpell = new IcicleBallSpell();
    private Spell shieldSpell = new ShatteringWallSpell();
    private Spell dashSpell = new FrozenSlideSpell();
    private Spell areaAttackSpell = new ChillingAirSpell();
    private Spell specialSpell = new FrigidGlareSpell();

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
