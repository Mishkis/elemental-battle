package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.ChillingAirEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ChillingAirSpell extends Spell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "chilling_air");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.FROST;
    }

    @Override
    public int getCooldown() {
        return 250;
    }

    @Override
    public String getDescription() {
        return "The air around you becomes freezing, and encases any nearby in solid ice.";
    }

    @Override
    public float getDamage() {
        return 3;
    }

    @Override
    public int getUptime() {
        return 20;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        world.spawnEntity(genericEntity(user, new ChillingAirEntity(ElementalBattleEntities.CHILLING_AIR, world)));
    }
}
