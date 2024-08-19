package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.flame_staff.ConeOfFireEntity;
import io.github.mishkis.elemental_battle.entity.flame_staff.FlameVortexEntity;
import io.github.mishkis.elemental_battle.entity.flame_staff.FlamingDashEntity;
import io.github.mishkis.elemental_battle.entity.flame_staff.WallOfFireEntity;
import io.github.mishkis.elemental_battle.item.helpers.MagicStaffItem;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.flame.ConeOfFireSpell;
import io.github.mishkis.elemental_battle.spells.flame.FlameVortexSpell;
import io.github.mishkis.elemental_battle.spells.flame.FlamingDashSpell;
import io.github.mishkis.elemental_battle.spells.flame.WallOfFireSpell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FlameStaff extends MagicStaffItem {
    public FlameStaff() {
        super("flame_staff", new Settings());
    }

    private Spell useSpell = new ConeOfFireSpell();
    private Spell shieldSpell = new WallOfFireSpell();
    private Spell dashSpell = new FlamingDashSpell();
    private Spell areaAttackSpell = new FlameVortexSpell();

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