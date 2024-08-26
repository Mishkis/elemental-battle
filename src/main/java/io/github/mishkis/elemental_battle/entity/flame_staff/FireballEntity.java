package io.github.mishkis.elemental_battle.entity.flame_staff;

import io.github.mishkis.elemental_battle.entity.MagicProjectileEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FireballEntity extends MagicProjectileEntity implements GeoEntity {
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.fireball.idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FireballEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);

        this.setNoGravity(true);
    }

    @Override
    public boolean isOnFire() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (200 < age) {
            explode();
            this.discard();
        }
    }

    @Override
    protected void playTravelParticle(double x, double y, double z) {
        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ElementalBattleParticles.FLAME_PARTICLE_PARTIAL, x + random.nextBetween(-10, 10) * 0.1, y + random.nextBetween(-10, 10) * 0.1, z + random.nextBetween(-10, 10) * 0.1, 0, 0, 0);
        }
    }

    @Override
    protected void playDiscardParticle(double x, double y, double z) {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ElementalBattleParticles.FIREBALL, x, y + 0.5, z, 3, 1, 0.2, 1, 1);
            serverWorld.spawnParticles(ElementalBattleParticles.FLAME_PARTICLE_FULL, x, y + 0.5, z, 5, 1, 1, 1, 2);
        }
    }

    private void explode() {
        this.getWorld().getOtherEntities(this.getOwner(), this.getBoundingBox().expand(3, 3, 3)).forEach((entity -> {
            entity.damage(this.getDamageSources().indirectMagic(this, this.getOwner()), 10 / entity.distanceTo(this));
            entity.setOnFireForTicks(40);
        }));
    }

    @Override
    protected void onBlockHit() {
        explode();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        explode();
        playDiscardParticle(this.getX(), this.getY(), this.getZ());
        this.discard();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", (animationState) -> animationState.setAndContinue(ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
