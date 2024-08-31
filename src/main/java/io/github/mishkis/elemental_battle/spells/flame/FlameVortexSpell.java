package io.github.mishkis.elemental_battle.spells.flame;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.flame_staff.FlameVortexEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FlameVortexSpell extends Spell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "flame_vortex");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.FLAME;
    }

    @Override
    public int getCooldown() {
        return 200;
    }

    @Override
    public String getDescription() {
        return "A vortex of pure fire is created surrounding you, drawing any in it's radius inwards.";
    }

    @Override
    public float getDamage() {
        return 5;
    }

    @Override
    public int getUptime() {
        return 20;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        world.spawnEntity(genericEntity(user, new FlameVortexEntity(ElementalBattleEntities.FLAME_VORTEX, world)));
    }
}
