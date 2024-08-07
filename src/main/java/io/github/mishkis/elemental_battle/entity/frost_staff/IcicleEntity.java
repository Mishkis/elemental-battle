package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicProjectileEntity;
import io.github.mishkis.elemental_battle.misc.ElementalBattleParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class IcicleEntity extends MagicProjectileEntity implements GeoEntity {
    private final RawAnimation SPAWN_ANIMATION = RawAnimation.begin().thenPlay("animation.icicle.spawn");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private PlayerEntity orbitTarget;
    private Vec3d orbitPoint;
    private float orbitSpeed = 0;
    private float yOffset;

    private Entity target;
    private boolean slowingDown;
    private double startAge;

    public IcicleEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setOrbit(PlayerEntity orbitTarget, float orbitSpeed, float yOffset) {
        this.orbitTarget = orbitTarget;
        this.orbitSpeed = orbitSpeed;
        this.yOffset = yOffset;

        this.orbitPoint = orbitTarget.getPos().relativize(this.getPos().offset(Direction.DOWN, yOffset));
    }

    public void setTarget(Entity target) {
        this.target = target;
        this.slowingDown = true;
    }

    @Override
    public void tick() {
        if (orbitTarget != null) {
            orbitPoint = orbitPoint.rotateY((float) Math.toRadians(orbitSpeed));
            Vec3d targetPos = orbitPoint.add(orbitTarget.getPos());
            Vec3d currentPos = this.getPos().offset(Direction.DOWN, yOffset);

            Vec3d pointedVector = targetPos.subtract(currentPos);

            this.setVelocity(this.getVelocity().lerp(pointedVector, 0.5));
        }
        else if (target != null) {
            if (slowingDown) {
                this.setVelocity(this.getVelocity().lerp(Vec3d.ZERO, 0.2));

                if (this.getVelocity().getY() <= 0) {
                    slowingDown = false;
                    this.setNoGravity(true);
                    startAge = age;
                };
            }
            else {
                double delta = age - startAge;
                delta *= 0.05;

                ElementalBattle.LOGGER.info(String.valueOf(delta));

                this.setVelocity(Vec3d.ZERO.lerp(target.getEyePos().subtract(this.getPos()), delta));
            }

            if (target.isRemoved()) {
                this.target = null;
                this.setNoGravity(false);
            }
        }
        super.tick();
    }

    @Override
    protected void playTravelParticle(double x, double y, double z) {
        this.getWorld().addParticle(ElementalBattleParticles.FROST_PARTICLE, x, y, z, 0, 0, 0);
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
}