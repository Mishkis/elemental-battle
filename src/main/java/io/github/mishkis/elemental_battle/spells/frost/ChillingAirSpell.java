package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.ChillingAirEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
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
    protected Integer getCooldown() {
        return 250;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        ChillingAirEntity chillingAir = new ChillingAirEntity(ElementalBattleEntities.CHILLING_AIR, world);

        chillingAir.setOwner(user);
        chillingAir.setUptime(20);
        chillingAir.setPosition(user.getPos().offset(Direction.UP, 1));

        world.spawnEntity(chillingAir);
    }
}
