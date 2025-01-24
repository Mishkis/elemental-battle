package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicShieldEntity;
import io.github.mishkis.elemental_battle.entity.frost_staff.ShatteringWallEntity;
import io.github.mishkis.elemental_battle.spells.ShieldSpell;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ShatteringWallSpell extends ShieldSpell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "shattering_wall");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.FROST;
    }

    @Override
    public int getCooldown() {
        return 200;
    }

    @Override
    public String getDescription() {
        return "Pure ice surrounds you, encasing and fully protecting you. If any foe strikes this barrier, they are hit by a shattered off icicle; this parry too creates enough energy for you to not freeze at the conclusion of the spell.";
    }

    @Override
    public float getDamage() {
        return 5;
    }

    @Override
    public int getUptime() {
        return 60;
    }

    @Override
    protected MagicShieldEntity getShieldEntity(World world) {
        return new ShatteringWallEntity(ElementalBattleEntities.SHATTERING_WALL, world);
    }
}
