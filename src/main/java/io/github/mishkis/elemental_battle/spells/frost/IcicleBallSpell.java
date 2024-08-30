package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.IcicleBallEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IcicleBallSpell extends Spell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "icicle_ball");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.FROST;
    }

    @Override
    protected Integer getCooldown() {
        return 10;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        Vec3d spawnPos = user.getEyePos().add(user.getRotationVector().multiply(3)).offset(Direction.DOWN, 0.2);

        IcicleBallEntity icicleBall = new IcicleBallEntity(ElementalBattleEntities.ICICLE_BALL, world);
        icicleBall.setPosition(spawnPos);

        icicleBall.setOwner(user);
        icicleBall.setDamage(5);
        icicleBall.setUptime(400);

        world.spawnEntity(icicleBall);
    }
}
