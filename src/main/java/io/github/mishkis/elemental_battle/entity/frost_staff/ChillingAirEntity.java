package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicAreaAttackEntity;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
    protected void onEntityCollision(LivingEntity entity) {
        if (entity.getStatusEffect(ElementalBattleStatusEffects.SHIELD_EFFECT) == null) {
            FrozenSolidEntity frozenSolid = new FrozenSolidEntity(ElementalBattleEntities.FROZEN_SOLID, this.getWorld());

            frozenSolid.setTarget(entity);
            frozenSolid.setPosition(entity.getPos());

            this.getWorld().spawnEntity(frozenSolid);
        }
        else {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 2), this);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", (animationState) -> (animationState.setAndContinue(ANIMATION))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
