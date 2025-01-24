package io.github.mishkis.elemental_battle.spells.air;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicShieldEntity;
import io.github.mishkis.elemental_battle.entity.air_staff.BlowBackEntity;
import io.github.mishkis.elemental_battle.spells.ShieldSpell;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BlowBackSpell extends ShieldSpell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "blow_back");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.AIR;
    }

    @Override
    public int getCooldown() {
        return 350;
    }

    @Override
    public String getDescription() {
        return "The winds encircle you, dancing away any attacks that would hit you and propelling their attackers away.";
    }

    @Override
    public int getUptime() {
        return 80;
    }

    @Override
    protected MagicShieldEntity getShieldEntity(World world) {
        return new BlowBackEntity(ElementalBattleEntities.BLOW_BACK, world);
    }
}
