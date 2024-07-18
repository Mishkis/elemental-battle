package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.entity.MagicProjectileEntity;
import io.github.mishkis.elemental_battle.misc.ElementalBattleParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class IcicleEntity extends MagicProjectileEntity implements GeoEntity {
    private final RawAnimation SPAWN_ANIMATION = RawAnimation.begin().thenPlay("animation.icicle.spawn");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    protected void playTravelParticle(double x, double y, double z) {
        this.getWorld().addParticle(ElementalBattleParticles.FROST_PARTICLE, x, y, z, 0, 0, 0);
    }

    public IcicleEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity entity = entityHitResult.getEntity();

        if (!this.getWorld().isClient && entity instanceof LivingEntity && entity != getOwner()) {
            entity.damage(this.getDamageSources().indirectMagic(this.getOwner(), entity), this.getDamage());

            ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 2), this);

            this.discard();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", this::spawnAnimation));
    }

    private <E extends IcicleEntity> PlayState spawnAnimation(final AnimationState<E> event) {
        return event.setAndContinue(SPAWN_ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}
}