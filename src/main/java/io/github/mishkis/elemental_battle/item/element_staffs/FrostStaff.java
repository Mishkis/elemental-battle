package io.github.mishkis.elemental_battle.item.element_staffs;

import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import io.github.mishkis.elemental_battle.spells.frost.*;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

public class FrostStaff extends MagicStaffItem {
    public FrostStaff() {
        super("frost_staff");
    }

    private final Spell useSpell = new IcicleBallSpell();

    @Override
    protected SpellElement getElement() {
        return SpellElement.FROST;
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
