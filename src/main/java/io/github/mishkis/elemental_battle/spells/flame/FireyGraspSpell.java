package io.github.mishkis.elemental_battle.spells.flame;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.flame_staff.FireyGraspEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FireyGraspSpell extends Spell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "firey_grasp");
    }

    @Override
    protected SpellElement getElement() {
        return SpellElement.FLAME;
    }

    @Override
    protected Integer getCooldown() {
        return 300;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        FireyGraspEntity fireyGrasp = new FireyGraspEntity(ElementalBattleEntities.FIREY_GRASP, world);

        fireyGrasp.setOwner(user);
        fireyGrasp.setPosition(user.getPos().offset(Direction.UP, 1));

        fireyGrasp.setNoGravity(true);
        fireyGrasp.setVelocity(user.getRotationVector(0, user.getYaw()).multiply(0.3));

        world.spawnEntity(fireyGrasp);
    }
}
