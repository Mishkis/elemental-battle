package io.github.mishkis.elemental_battle.spells.flame;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.flame_staff.WallOfFireEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class WallOfFireSpell extends Spell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "wall_of_fire");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.FLAME;
    }

    @Override
    protected Integer getCooldown() {
        return 300;
    }

    @Override
    public int getUptime() {
        return 80;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        world.spawnEntity(genericEntity(user, new WallOfFireEntity(ElementalBattleEntities.WALL_OF_FIRE, world)));
    }
}
