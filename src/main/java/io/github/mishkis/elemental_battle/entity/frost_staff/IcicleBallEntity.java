package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.misc.ElementalBattleParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.particle.ParticleEffect;
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

    private boolean isHit = false;

    public IcicleBallEntity(EntityType<? extends IcicleBallEntity> entityType, World world) {
        super(entityType, world);
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
        World world = this.getWorld();

        super.onCollision(hitResult);
        if (!world.isClient) {
            this.explode(world);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        World world = this.getWorld();

        super.onEntityHit(entityHitResult);
        if (!world.isClient) {
            this.explode(world);
        }
    }

    private void explode(World world) {
        int spawnCount = 8;

        for (int i = 0; i < spawnCount; i++) {
            Vec3d spawnPos = this.getPos();

            IcicleEntity icicle = new IcicleEntity(ElementalBattleEntities.ICICLE, world);
            icicle.setPosition(spawnPos);

            Vec3d velocity = new Vec3d(0.2, 0.3, 0);
            icicle.setVelocity(velocity.rotateY((float) ((Math.PI * 2 * i)/spawnCount)));
            world.spawnEntity(icicle);
        }
        this.discard();
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
