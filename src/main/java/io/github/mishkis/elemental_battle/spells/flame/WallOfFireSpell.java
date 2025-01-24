package io.github.mishkis.elemental_battle.spells.flame;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicShieldEntity;
import io.github.mishkis.elemental_battle.entity.flame_staff.WallOfFireEntity;
import io.github.mishkis.elemental_battle.spells.ShieldSpell;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class WallOfFireSpell extends ShieldSpell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "wall_of_fire");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.FLAME;
    }

    @Override
    public int getCooldown() {
        return 30;
    }

    @Override
    public String getDescription() {
        return "Scalding flames dance around you, protecting you from attackers.";
    }

    @Override
    public int getUptime() {
        return 80;
    }

    @Override
    protected MagicShieldEntity getShieldEntity(World world) {
        return new WallOfFireEntity(ElementalBattleEntities.WALL_OF_FIRE, world);
    }
}
