package io.github.mishkis.elemental_battle.spells.flame;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.flame_staff.FlamingDashEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class FlamingDashSpell extends Spell {
    @Override
    protected Elements getElement() {
        return Elements.FLAME;
    }

    @Override
    protected Integer getCooldown() {
        return 100;
    }

    @Override
    protected boolean onCast(World world, PlayerEntity user) {
        FlamingDashEntity flamingDash = new FlamingDashEntity(ElementalBattleEntities.FLAMING_DASH, world);

        flamingDash.setOwner(user);
        flamingDash.setPosition(user.getPos());

        world.spawnEntity(flamingDash);

        return true;
    }
}
