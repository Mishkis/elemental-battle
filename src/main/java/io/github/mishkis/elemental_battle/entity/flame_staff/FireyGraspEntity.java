package io.github.mishkis.elemental_battle.entity.flame_staff;

import io.github.mishkis.elemental_battle.entity.MagicProjectileEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FireyGraspEntity extends MagicProjectileEntity implements GeoEntity {
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.firey_grasp.spawn");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private Entity hitEntity = null;

    public FireyGraspEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void playTravelParticle(double x, double y, double z) {
        if (!this.getWorld().isClient) {
            this.getWorld().addParticle(ElementalBattleParticles.FLAME_PARTICLE_SMOKE, x + random.nextBetween(-5, 5) * 0.1, y + random.nextBetween(5, 15) * 0.1, z + random.nextBetween(-5, 5) * 0.1, 0, 0, 0);
        }
    }

    @Override
    protected void playDiscardParticle(double x, double y, double z) {}

    @Override
    public void tick() {
        super.tick();

        if (100 < age) {
            this.discard();
        }

        if (hitEntity != null && this.isAlive()) {
            hitEntity.setPosition(this.getPos());
            hitEntity.setVelocity(Vec3d.ZERO);

            if (hitEntity instanceof ServerPlayerEntity player) {
                player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (hitEntity == null && entityHitResult.getEntity() instanceof LivingEntity entity && entity != this.getOwner() && !this.getWorld().isClient) {
            hitEntity = entity;

            this.setPosition(hitEntity.getPos());
            this.setVelocity(Vec3d.ZERO.offset(Direction.UP, 0.01));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", (animationState) -> animationState.setAndContinue(ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
