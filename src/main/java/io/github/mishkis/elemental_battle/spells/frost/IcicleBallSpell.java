package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.IcicleBallEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IcicleBallSpell extends Spell {
    @Override
    protected Elements getElement() {
        return Elements.FROST;
    }

    @Override
    protected Integer getCooldown() {
        return 40;
    }

    @Override
    protected boolean onCast(World world, PlayerEntity user) {
        Vec3d spawnPos = user.getEyePos().add(user.getRotationVector().multiply(3));

        IcicleBallEntity icicleBall = new IcicleBallEntity(ElementalBattleEntities.ICICLE_BALL, world);
        icicleBall.setPosition(spawnPos);

        icicleBall.setOwner(user);
        icicleBall.setDamage(5);

        world.spawnEntity(icicleBall);

        return true;
    }
}
