package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicShieldEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ShatteringWallEntity extends MagicShieldEntity implements GeoEntity {
    private final RawAnimation SPAWN_ANIMATION = RawAnimation.begin().thenPlay("animation.shattering_wall.spawn").thenLoop("animation.shattering_wall.idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ShatteringWallEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void playParticle(Vec3d pos) {
        this.getWorld().addParticle(ElementalBattleParticles.FROST_PARTICLE, pos.getX() + random.nextBetween(-1, 1), pos.getY() + 1, pos.getZ() + random.nextBetween(-1, 1), 0, random.nextBetween(1, 3) * 0.1, 0);
    }

    @Override
    public void onTimeOut(PlayerEntity owner) {
        if (owner.getStatusEffect(ElementalBattleStatusEffects.SUCCESSFUL_PARRY_EFFECT) == null) {
            owner.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 2));
        }
    }

    @Override
    public void onDamaged(DamageSource source) {
        World world = this.getWorld();

        if (!world.isClient()) {
            PlayerEntity owner = this.getOwner();
            if (owner != null) {
                Vec3d particlePos = owner.getEyePos().add(owner.getRotationVector().multiply(0.5));
                ((ServerWorld) world).spawnParticles(ElementalBattleParticles.FROST_SHATTER_PARTICLE, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1, 0, 0, 0, 1);

                owner.setStatusEffect(new StatusEffectInstance(ElementalBattleStatusEffects.SUCCESSFUL_PARRY_EFFECT, 100, 0), this);

            }

            IcicleEntity icicle = new IcicleEntity(ElementalBattleEntities.ICICLE, world);

            // Make icicle target hit source.
            Entity target = null;
            Entity entity = source.getSource();
            if (entity instanceof Ownable) {
                target = ((Ownable) entity).getOwner();
            }
            else if (entity instanceof LivingEntity) {
                target = entity;
            }

            if (target != null && target != this.getOwner()) {
                icicle.setTarget(target);

                // Spawn a target on the entity
                IceTargetEntity iceTarget = new IceTargetEntity(ElementalBattleEntities.ICE_TARGET, world);

                iceTarget.setTarget(target);
                iceTarget.setPosition(target.getPos());

                world.spawnEntity(iceTarget);
            }

            icicle.setOwner(this.getOwner());
            icicle.setDamage(5);
            icicle.setPosition(this.getPos().offset(Direction.UP, 1));

            icicle.setVelocity(random.nextBetween(-10, 10) * 0.1, 0.3, random.nextBetween(-10, 10) * 0.1);

            this.getWorld().spawnEntity(icicle);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", this::spawnAnimation));
    }

    private <E extends ShatteringWallEntity> PlayState spawnAnimation(final AnimationState<E> event) {
        return event.setAndContinue(SPAWN_ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
