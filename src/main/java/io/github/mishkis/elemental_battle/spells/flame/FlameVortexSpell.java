package io.github.mishkis.elemental_battle.spells.flame;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.flame_staff.FlameVortexEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FlameVortexSpell extends Spell {
    @Override
    protected Elements getElement() {
        return Elements.FLAME;
    }

    @Override
    public Identifier getIcon() {
        return null;
    }

    @Override
    protected Integer getCooldown() {
        return 200;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        FlameVortexEntity flameVortex = new FlameVortexEntity(ElementalBattleEntities.FLAME_VORTEX, world);

        flameVortex.setOwner(user);
        flameVortex.setPosition(user.getPos().offset(Direction.UP, 1));

        world.spawnEntity(flameVortex);
    }
}
