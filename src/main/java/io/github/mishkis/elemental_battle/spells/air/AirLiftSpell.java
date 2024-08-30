package io.github.mishkis.elemental_battle.spells.air;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.air_staff.AirLiftEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class AirLiftSpell extends Spell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "air_lift");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.AIR;
    }

    @Override
    protected Integer getCooldown() {
        return 200;
    }

    @Override
    public int getUptime() {
        return 20;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        world.spawnEntity(genericEntity(user, new AirLiftEntity(ElementalBattleEntities.AIR_LIFT, world)));
    }
}
