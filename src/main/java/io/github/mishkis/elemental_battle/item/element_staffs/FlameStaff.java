package io.github.mishkis.elemental_battle.item.element_staffs;

import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import io.github.mishkis.elemental_battle.spells.flame.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public class FlameStaff extends MagicStaffItem {
    public FlameStaff() {
        super("flame_staff");
    }

    private final Spell useSpell = new ConeOfFireSpell();

    @Override
    public SpellElement getElement() {
        return SpellElement.FLAME;
    }

    @Override
    protected void hitAttack(LivingEntity target, PlayerEntity player) {
        target.setOnFireForTicks(60);
    }

    @Override
    public @Nullable Spell useSpell() {
        return useSpell;
    }

    @Override
    public @Nullable Spell ultimateSpell() {
        return new ConeOfFireSpell();
    }
}