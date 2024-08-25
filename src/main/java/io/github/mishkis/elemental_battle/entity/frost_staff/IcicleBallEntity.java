package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class IcicleBallEntity extends AbstractFireballEntity implements GeoEntity {
    private final RawAnimation SPAWN_ANIMATION = RawAnimation.begin().thenPlay("animation.icicle_ball.spawn").thenLoop("animation.icicle_ball.idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private float damage;

    private boolean isHit = false;

    public IcicleBallEntity(EntityType<? extends IcicleBallEntity> entityType, World world) {
        super(entityType, world);
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    protected boolean isBurning() {
        return false;
    }

    @Override
    protected ParticleEffect getParticleType() {
        return ElementalBattleParticles.FROST_PARTICLE;
    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity deflector, @Nullable Entity owner, boolean fromAttack) {
        if (!isHit) {
            isHit = true;
            return super.deflect(deflection, deflector, owner, fromAttack);
        }
        return false;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        World world = this.getWorld();
        if (!world.isClient) {
            this.explode((ServerWorld) world);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

       Entity entity = entityHitResult.getEntity();

        if (entity instanceof LivingEntity) {
            entity.damage(this.getDamageSources().indirectMagic(this.getOwner(), entity), this.getDamage() * 2);

            ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 2), this);
        }
    }

    private void explode(ServerWorld world) {
        world.spawnParticles(ElementalBattleParticles.FROST_SHATTER_PARTICLE, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 1);

        int spawnCount = 8 + random.nextBetween(-1, 1);

        double startRotation = Math.toRadians(random.nextInt(45));

        for (int i = 0; i < spawnCount; i++) {
            Vec3d spawnPos = this.getPos().add(0, 0.2, 0);

            IcicleEntity icicle = new IcicleEntity(ElementalBattleEntities.ICICLE, world);
            icicle.setPosition(spawnPos);

            Vec3d velocity = new Vec3d(0.2, 0.3, 0);
            icicle.setVelocity(velocity.rotateY((float) (startRotation + (Math.PI * 2 * i)/spawnCount)));

            icicle.setDamage(this.getDamage());

            icicle.setOwner(this.getOwner());

            world.spawnEntity(icicle);
        }

        this.discard();
    }

    @Override
    public void tick() {
        super.tick();

        if (800 < age && this.getWorld() instanceof ServerWorld serverWorld) {
            this.explode(serverWorld);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", this::spawnAnimation));
    }

    private <E extends IcicleBallEntity> PlayState spawnAnimation(final AnimationState<E> event) {
        return event.setAndContinue(SPAWN_ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
