package io.github.mishkis.elemental_battle.entity.flame_staff;

import io.github.mishkis.elemental_battle.entity.MagicProjectileEntity;
import io.github.mishkis.elemental_battle.misc.ElementalBattleParticles;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class ConeOfFireEntity extends MagicProjectileEntity implements GeoEntity {
    private final RawAnimation SPAWN_ANIMATION = RawAnimation.begin().thenPlay("animation.cone_of_fire.idle");
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public ConeOfFireEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void playTravelParticle(double x, double y, double z) {
        SimpleParticleType particle;
        if (age < 10) {
            particle = ElementalBattleParticles.FLAME_PARTICLE_FULL;
        } else if (age < 20) {
            particle = ElementalBattleParticles.FLAME_PARTICLE_PARTIAL;
        } else {
            particle = ElementalBattleParticles.FLAME_PARTICLE_SMOKE;
        }

        this.getWorld().addParticle(particle, x, y, z, 0, 0, 0);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", this::spawnAnimation));
    }

    private <E extends ConeOfFireEntity> PlayState spawnAnimation(final AnimationState<E> event) {
        return event.setAndContinue(SPAWN_ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
