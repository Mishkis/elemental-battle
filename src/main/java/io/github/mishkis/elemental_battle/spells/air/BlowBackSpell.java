package io.github.mishkis.elemental_battle.spells.air;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.air_staff.BlowBackEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BlowBackSpell extends Spell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "blow_back");
    }

    @Override
    protected SpellElement getElement() {
        return SpellElement.AIR;
    }

    @Override
    protected Integer getCooldown() {
        return 350;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        BlowBackEntity blowBack = new BlowBackEntity(ElementalBattleEntities.BLOW_BACK, world);

        blowBack.setOwner(user);
        blowBack.setUptime(80);

        world.spawnEntity(blowBack);
    }
}
