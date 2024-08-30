package io.github.mishkis.elemental_battle.spells.flame;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.flame_staff.FlamingDashEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FlamingDashSpell extends Spell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "flaming_dash");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.FLAME;
    }

    @Override
    protected Integer getCooldown() {
        return 100;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        FlamingDashEntity flamingDash = new FlamingDashEntity(ElementalBattleEntities.FLAMING_DASH, world);

        flamingDash.setOwner(user);
        flamingDash.setUptime(20);
        flamingDash.setPosition(user.getPos());

        world.spawnEntity(flamingDash);
    }
}
