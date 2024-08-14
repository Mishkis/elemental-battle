package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicAreaAttackEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ChillingAirEntity extends MagicAreaAttackEntity implements GeoEntity {
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.chilling_air.spawn");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ChillingAirEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected int getUptime() {
        return 20;
    }

    @Override
    protected void onEntityCollision(LivingEntity entity) {
        FrozenSolidEntity frozenSolid = new FrozenSolidEntity(ElementalBattleEntities.FROZEN_SOLID, this.getWorld());

        frozenSolid.setTarget(entity);
        frozenSolid.setPosition(entity.getPos());

        this.getWorld().spawnEntity(frozenSolid);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", this::animation));
    }

    public <E extends ChillingAirEntity> PlayState animation(AnimationState<E> animationState) {
        return animationState.setAndContinue(ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
