package io.github.mishkis.elemental_battle.spells.flame;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicEntity;
import io.github.mishkis.elemental_battle.entity.flame_staff.FireballEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FireballSpell extends Spell {
    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "fireball");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.FLAME;
    }

    @Override
    protected Integer getCooldown() {
        return 300;
    }

    @Override
    public float getDamage() {
        return 10;
    }

    @Override
    public int getUptime() {
        return 400;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        MagicEntity fireball = genericEntity(user, new FireballEntity(ElementalBattleEntities.FIREBALL, world));

        fireball.setVelocity(user.getRotationVector().multiply(2));
        fireball.setPosition(user.getPos().offset(Direction.UP, 1.3).add(fireball.getVelocity()));

        world.spawnEntity(fireball);
    }
}
