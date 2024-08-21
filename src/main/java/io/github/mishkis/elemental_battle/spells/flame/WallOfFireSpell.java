package io.github.mishkis.elemental_battle.spells.flame;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.flame_staff.WallOfFireEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class WallOfFireSpell extends Spell {
    @Override
    protected SpellElement getElement() {
        return SpellElement.FLAME;
    }

    @Override
    public Identifier getIcon() {
        return null;
    }

    @Override
    protected Integer getCooldown() {
        return 300;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        WallOfFireEntity wallOfFire = new WallOfFireEntity(ElementalBattleEntities.WALL_OF_FIRE, world);

        wallOfFire.setOwner(user);
        wallOfFire.setUptime(100);

        world.spawnEntity(wallOfFire);
    }
}
