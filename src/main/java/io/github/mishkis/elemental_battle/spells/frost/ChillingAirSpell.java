package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.ChillingAirEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ChillingAirSpell extends Spell {
    @Override
    protected Elements getElement() {
        return Elements.FROST;
    }

    @Override
    protected Integer getCooldown() {
        return 200;
    }

    @Override
    protected boolean onCast(World world, PlayerEntity user) {
        ChillingAirEntity chillingAir = new ChillingAirEntity(ElementalBattleEntities.CHILLING_AIR, world);

        chillingAir.setOwner(user);
        chillingAir.setPosition(user.getPos().offset(Direction.UP, 1));

        world.spawnEntity(chillingAir);

        return true;
    }
}
