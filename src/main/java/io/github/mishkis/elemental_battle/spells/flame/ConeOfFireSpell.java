package io.github.mishkis.elemental_battle.spells.flame;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicEntity;
import io.github.mishkis.elemental_battle.entity.flame_staff.ConeOfFireEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ConeOfFireSpell extends Spell {
    private final Random random = Random.create();

    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "cone_of_fire");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.FLAME;
    }

    @Override
    public int getCooldown() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "Fire envelops your staff, spewing forth infernal flames to burn your foes.";
    }

    @Override
    public float getDamage() {
        return 5;
    }

    @Override
    public int getUptime() {
        return 15;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        Vec3d spawnPos = user.getEyePos();

        int spawnCount = 8 + random.nextBetween(-1, 1);

        float randomSpread = random.nextBetween(-1, 1) * 0.1F;
        for (int i = 0; i < spawnCount; i++) {
            MagicEntity coneOfFire = genericEntity(user, new ConeOfFireEntity(ElementalBattleEntities.CONE_OF_FIRE, world));
            coneOfFire.setPosition(spawnPos);

            coneOfFire.setNoGravity(true);

            Vec3d offset = new Vec3d(0.3 + randomSpread, 0, 0).rotateZ((float) (2 * Math.PI * i / spawnCount));

            offset = offset.rotateX((float) Math.toRadians(-user.getPitch()));
            offset = offset.rotateY((float) Math.toRadians(-user.getYaw()));

            Vec3d velocity = user.getRotationVector().add(offset);

            velocity = velocity.normalize();
            velocity = velocity.multiply(0.5);
            coneOfFire.setVelocity(velocity);

            world.spawnEntity(coneOfFire);
        }
    }
}
