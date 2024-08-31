package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicEntity;
import io.github.mishkis.elemental_battle.entity.frost_staff.IcicleBallEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
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
    public int getCooldown() {
        return 10;
    }

    @Override
    public String getDescription() {
        return "A spell which summons a ball of icicles which shatter upon impact, chilling the foe.";
    }

    @Override
    public float getDamage() {
        return 5;
    }

    @Override
    public int getUptime() {
        return 400;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        MagicEntity icicleBall = genericEntity(user, new IcicleBallEntity(ElementalBattleEntities.ICICLE_BALL, world));

        icicleBall.setPosition(user.getEyePos().add(user.getRotationVector().multiply(3)).offset(Direction.DOWN, 0.2));

        world.spawnEntity(icicleBall);
    }
}
