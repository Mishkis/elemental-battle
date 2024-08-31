package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.TargetableMagicEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.rendering.SpellDisplay;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class IceTargetEntity extends TargetableMagicEntity implements GeoEntity {
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.ice_target.spawn").thenPlay("animation.ice_target.despawn");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // Controls whether this should summon a Frozen Solid entity or not.
    private Boolean freezing = false;

    public IceTargetEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void setTarget(Entity target) {
        super.setTarget(target);

        if (target != null) {
            target.setAttached(SpellDisplay.SPELL_DISPLAY_SHIELD_WARNING_ATTACHMENT, true);
        }
    }

    // This has a constant uptime.
    // 1.25 is the exact time it takes for ANIMATION to finish. Multiply by 20, add a little so it can finish.
    @Override
    public Integer getUptime() {
        return 26;
    }

    public void setFreezing() {
        this.freezing = true;
    }

    @Override
    public void tick() {
        super.tick();

        Entity target = this.getTarget();
        if (target != null && !target.isRemoved()) {
            this.setPosition(target.getPos());
            if (this.getWorld().isClient()) {
                this.getWorld().addParticle(ElementalBattleParticles.FROST_PARTICLE, this.getX() + random.nextBetween(-1, 1), this.getY() + 1, this.getZ() + random.nextBetween(-1, 1), 0, random.nextBetween(1, 3) * 0.1, 0);
            }

            // Do this before time out to ensure that it is in fact called.
            if (getUptime() - 1 < age && this.getWorld().isClient) {
                target.removeAttached(SpellDisplay.SPELL_DISPLAY_SHIELD_WARNING_ATTACHMENT);
            }
        }
        else {
            this.discard();
        }
    }

    @Override
    protected void onTimeOut() {
        if (!this.getWorld().isClient) {
            if (freezing && this.getTarget() instanceof LivingEntity livingTarget && livingTarget.getStatusEffect(ElementalBattleStatusEffects.SHIELD_EFFECT) == null) {
                FrozenSolidEntity frozenSolid = new FrozenSolidEntity(ElementalBattleEntities.FROZEN_SOLID, this.getWorld());

                frozenSolid.setOwner(this.getOwner());
                frozenSolid.setTarget(livingTarget);
                frozenSolid.setDamage(this.getDamage());
                frozenSolid.setPosition(livingTarget.getPos());

                this.getWorld().spawnEntity(frozenSolid);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", this::spawnAnimation));
    }

    private <E extends IceTargetEntity> PlayState spawnAnimation(final AnimationState<E> event) {
        return event.setAndContinue(ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
