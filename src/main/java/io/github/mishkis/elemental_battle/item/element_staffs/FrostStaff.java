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

    private final Spell useSpell = new IcicleBallSpell();
    private final Spell shieldSpell = new ShatteringWallSpell();
    private final Spell dashSpell = new FrozenSlideSpell();
    private final Spell areaAttackSpell = new ChillingAirSpell();
    private final Spell specialSpell = new FrigidGlareSpell();

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
