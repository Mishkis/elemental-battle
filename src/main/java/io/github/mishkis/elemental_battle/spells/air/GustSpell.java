package io.github.mishkis.elemental_battle.spells.air;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.air_staff.GustEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GustSpell extends Spell {
    @Override
    protected SpellElement getElement() {
        return SpellElement.AIR;
    }

    @Override
    public Identifier getIcon() {
        return null;
    }

    @Override
    protected Integer getCooldown() {
        return 50;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        GustEntity gust = new GustEntity(ElementalBattleEntities.GUST, world);

        gust.setOwner(user);
        gust.setParentSpell(this);

        gust.setVelocity(user.getRotationVector());
        gust.setPosition(user.getEyePos().add(user.getRotationVector()));

        gust.setNoGravity(true);

        if (user.getAttached(GustEntity.EMPOWERED_ATTACHMENT) != null) {
            if (user.shouldIgnoreFallDamageFromCurrentExplosion() && !user.isOnGround()) {
                gust.setEmpowered();

                // Summons an additional 2 gust entities.
                for(int i = 0; i < 2; i++) {
                    // All of this code is reused, might be worthwhile to simplify it so no repeating, but can't be bothered rn
                    GustEntity surroundingGusts = new GustEntity(ElementalBattleEntities.GUST, world);

                    surroundingGusts.setOwner(user);
                    surroundingGusts.setParentSpell(this);

                    // Taken from cone of fire, maybe make a helper class.
                    Vec3d offset = new Vec3d(0.4, 0, 0).rotateZ((float) (Math.PI * i));

                    offset = offset.rotateX((float) Math.toRadians(-user.getPitch()));
                    offset = offset.rotateY((float) Math.toRadians(-user.getYaw()));

                    Vec3d velocity = user.getRotationVector().add(offset);

                    surroundingGusts.setVelocity(velocity);
                    surroundingGusts.setPosition(user.getEyePos().add(velocity));

                    surroundingGusts.setNoGravity(true);

                    surroundingGusts.setEmpowered();

                    world.spawnEntity(surroundingGusts);

                }
            }

            user.removeAttached(GustEntity.EMPOWERED_ATTACHMENT);
        }

        world.spawnEntity(gust);
    }
}
