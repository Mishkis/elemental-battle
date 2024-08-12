package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicDashEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FrozenSlideEntity extends MagicDashEntity implements GeoEntity {
    private final RawAnimation SPAWN_ANIMATION = RawAnimation.begin().thenPlay("animation.frozen_slide.spawn");
    private final RawAnimation EMPOWERED_ANIMATION = RawAnimation.begin().thenPlay("animation.frozen_slide.spawn").thenLoop("animation.frozen_slide.empowered");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean empowered = false;

    public FrozenSlideEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public void setEmpowered(boolean empowered) {
        this.empowered = empowered;
    }

    @Override
    public float getBlocksTraveled() {
        return 5 * (empowered ? 2 : 1);
    }

    @Override
    public float getUptime() {
        return 20;
    }

    @Override
    public boolean isGrounded() {
        return true;
    }

    @Override
    public void playParticle(Vec3d pos) {
        this.getWorld().addParticle(ElementalBattleParticles.FROST_PARTICLE, pos.getX() + random.nextBetween(-1, 1), pos.getY() + 1, pos.getZ() + random.nextBetween(-1, 1), 0, random.nextBetween(1, 3) * 0.1, 0);
    }

    @Override
    public void tick() {
        PlayerEntity owner = this.getOwner();

        if (!empowered && owner != null && owner.getStatusEffect(ElementalBattleStatusEffects.SUCCESSFUL_PARRY_EFFECT) != null) {
            this.setEmpowered(true);

            World world = this.getWorld();

            if (!world.isClient()) {
                // Play shatter particle.
                ((ServerWorld) world).spawnParticles(ElementalBattleParticles.FROST_SHATTER_PARTICLE, owner.getX(), owner.getEyeY(), owner.getZ(), 3, 1, 0.3, 1, 1);

                owner.removeStatusEffect(ElementalBattleStatusEffects.SUCCESSFUL_PARRY_EFFECT);

                // Summon icicle surrounding orbit.
                int spawnCount = 8;
                for (int i = 0; i < spawnCount; i++) {
                    IcicleEntity icicle = new IcicleEntity(ElementalBattleEntities.ICICLE, world);

                    icicle.setOwner(owner);

                    int rotationSpeed = 3;
                    float yOffset = 0.8F;
                    float spawnDistance = 1.5F;

                    if (i >= spawnCount / 2) {
                        rotationSpeed *= 2;
                        spawnDistance *= 2;
                    }

                    Vec3d spawnNormal = owner.getRotationVector().multiply(1, 0, 1).normalize();
                    if (spawnNormal == Vec3d.ZERO) {
                        spawnNormal = new Vec3d(1, 0, 0);
                    }
                    spawnNormal = spawnNormal.rotateY((float) (4 * i * Math.PI / spawnCount)).multiply(spawnDistance);

                    Vec3d spawnPos = owner.getPos().add(spawnNormal).offset(Direction.UP, yOffset);
                    icicle.setPosition(spawnPos);

                    icicle.setOrbit(owner, rotationSpeed, yOffset);
                    icicle.setNoGravity(true);

                    icicle.setUptime(5 * 20);

                    icicle.setVelocity(0.01, 0, 0); // for visual rotation on first tick spawned

                    icicle.setDamage(5);

                    world.spawnEntity(icicle);
                }
            }
        }

        if (!this.getWorld().isClient() && owner != null) {
            owner.setStatusEffect(new StatusEffectInstance(ElementalBattleStatusEffects.SHIELD_EFFECT, 10, 0), this);
        }

        super.tick();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", this::animation));
    }

    private <E extends FrozenSlideEntity> PlayState animation(final AnimationState<E> event) {
        if (empowered) {
            return event.setAndContinue(EMPOWERED_ANIMATION);
        }
        return event.setAndContinue(SPAWN_ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
