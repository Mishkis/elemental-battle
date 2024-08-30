package io.github.mishkis.elemental_battle.entity.flame_staff;

import io.github.mishkis.elemental_battle.entity.MagicEntity;
import io.github.mishkis.elemental_battle.entity.MagicProjectileEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class ConeOfFireEntity extends MagicProjectileEntity implements GeoEntity {
    private final RawAnimation SPAWN_ANIMATION = RawAnimation.begin().thenPlay("animation.cone_of_fire.idle");
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public ConeOfFireEntity(EntityType<? extends MagicEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void playTravelParticle(double x, double y, double z) {
        if (age > 2) {
            SimpleParticleType particle;
            if (age < this.getUptime()/3) {
                particle = ElementalBattleParticles.FLAME_PARTICLE_FULL;
            } else if (age < 2 * this.getUptime()/3) {
                particle = ElementalBattleParticles.FLAME_PARTICLE_PARTIAL;
            } else {
                particle = ElementalBattleParticles.FLAME_PARTICLE_SMOKE;
            }

            this.getWorld().addParticle(particle, x + random.nextBetween(-10, 10) * 0.01, y + random.nextBetween(-10, 10) * 0.01, z + random.nextBetween(-10, 10) * 0.01,  0, 0, 0);
        }
    }

    @Override
    protected void playDiscardParticle(double x, double y, double z) {}

    @Override
    protected void onEntityHit(Entity entity) {
        if (entity != getOwner() && !(entity instanceof ConeOfFireEntity coneOfFire && coneOfFire.getOwner() == this.getOwner())) {
            entity.damage(this.getDamageSources().indirectMagic(this, this.getOwner()), this.getDamage());
            entity.setOnFireForTicks(40);

            super.onEntityHit(entity);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", (animationState) -> animationState.setAndContinue(SPAWN_ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
