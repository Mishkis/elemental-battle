package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.entity.TargetableMagicEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FrozenSolidEntity extends TargetableMagicEntity implements GeoEntity {
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.frozen_solid.spawn");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private float ownerStartHealth = 0;

    public FrozenSolidEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public Integer getUptime() {
        return 100;
    }

    private void playShatterParticle(ServerWorld world, double x, double y, double z) {
        world.spawnParticles(ElementalBattleParticles.FROST_SHATTER_PARTICLE, x, y + 1, z, 3, 1, 0.2, 1, 1);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getTarget() instanceof LivingEntity target && !target.isRemoved()) {
            if (ownerStartHealth == 0) {
                ownerStartHealth = target.getHealth();
            }
            target.setStatusEffect(new StatusEffectInstance(ElementalBattleStatusEffects.SPELL_LOCK_EFFECT, 2, 0), this);
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 4), this);

            target.setVelocity(new Vec3d(0, target.isOnGround() ? 0 : target.getVelocity().y - 0.1, 0));

            if (target instanceof HostileEntity hostileTarget) {
                hostileTarget.setAiDisabled(true);

                // No ai disables all movement calculation, so this just reimplements it.
                hostileTarget.move(MovementType.SELF, hostileTarget.getVelocity());
                hostileTarget.fallDistance = 0;
            }

            if (target.isOnGround() || this.isOnGround()) {
                this.setPosition(target.getPos());
            }
            else {
                this.move(MovementType.SELF, target.getVelocity());
            }

            if (!this.getWorld().isClient && target.getHealth() < ownerStartHealth) {
                onTimeOut();
                this.discard();
            }

        }
        else {
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                playShatterParticle(serverWorld, this.getX(), this.getY(), this.getZ());
            }

            this.discard();
        }
    }

    @Override
    protected void onTimeOut() {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            if (this.getTarget() instanceof HostileEntity hostileTarget) {
                hostileTarget.setAiDisabled(false);
            }

            this.getTarget().damage(this.getDamageSources().indirectMagic(this, this.getOwner()), this.getDamage());

            playShatterParticle(serverWorld, this.getX(), this.getY(), this.getZ());
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        Entity entity = source.getSource();
        if (entity instanceof Ownable ownable) {
            entity = ownable.getOwner();
        }

        if (entity == this.getTarget()) {
            return false;
        }

        return this.getTarget().damage(source, amount);
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", (animatonState) -> animatonState.setAndContinue(ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
