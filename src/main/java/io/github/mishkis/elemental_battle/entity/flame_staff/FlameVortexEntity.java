package io.github.mishkis.elemental_battle.entity.flame_staff;

import io.github.mishkis.elemental_battle.entity.MagicAreaAttackEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FlameVortexEntity extends MagicAreaAttackEntity implements GeoEntity {
    RawAnimation animation = RawAnimation.begin().thenPlay("animation.flame_vortex.spawn");
    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FlameVortexEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected int getUptime() {
        return 20;
    }

    @Override
    protected void onEntityCollision(LivingEntity entity) {
        entity.damage(this.getDamageSources().indirectMagic(this.getOwner(), entity), 5);
        entity.setOnFireForTicks(60);

        entity.setVelocity(this.getOwner().getPos().subtract(entity.getPos()).normalize());
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", this::animation));
    }

    public <E extends FlameVortexEntity> PlayState animation(AnimationState<E> animationState) {
        return animationState.setAndContinue(animation);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
