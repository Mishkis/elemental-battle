package io.github.mishkis.elemental_battle.entity.flame_staff;

import io.github.mishkis.elemental_battle.entity.MagicProjectileEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
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
        World world = this.getWorld();
        if (!world.isClient() && age > 1) {
            SimpleParticleType particle;
            if (age < 5) {
                particle = ElementalBattleParticles.FLAME_PARTICLE_FULL;
            } else if (age < 10) {
                particle = ElementalBattleParticles.FLAME_PARTICLE_PARTIAL;
            } else {
                particle = ElementalBattleParticles.FLAME_PARTICLE_SMOKE;
            }

            ((ServerWorld) world).spawnParticles(particle, x, y, z, 1, 0.1, 0.1, 0.1, 1);
        }
    }

    @Override
    protected void playDiscardParticle(double x, double y, double z) {}

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity entity = entityHitResult.getEntity();

        if (!this.getWorld().isClient && entity != getOwner()) {
            entity.damage(this.getDamageSources().indirectMagic(this, this.getOwner()), this.getDamage());
            entity.setOnFireForTicks(40);

            this.discard();
        }
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
