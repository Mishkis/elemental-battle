package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.air_staff.GustEntity;
import io.github.mishkis.elemental_battle.item.helpers.MagicStaffItem;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.air.GustSpell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AirStaff extends MagicStaffItem {
    public AirStaff() {
        super("air_staff", new Settings());
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
