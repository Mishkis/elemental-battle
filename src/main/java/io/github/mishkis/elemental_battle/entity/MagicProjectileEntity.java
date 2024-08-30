package io.github.mishkis.elemental_battle.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class MagicProjectileEntity extends MagicEntity {
    protected abstract void playTravelParticle(double x, double y, double z);

    protected abstract void playDiscardParticle(double x, double y, double z);

    public MagicProjectileEntity(EntityType<? extends MagicEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public double getGravity() {
        return 0.05;
    }

    @Override
    public void tick() {
        super.tick();

        this.applyGravity();

        // Movement
        Vec3d velocity = this.getVelocity();
        double x = this.getX() + velocity.x;
        double y = this.getY() + velocity.y;
        double z = this.getZ() + velocity.z;

        // Calculate rotation of projectile to make it face towards moving direction.
        double baseAngle = Math.abs(velocity.x) + Math.abs(velocity.z);
        boolean negative = velocity.z < 0;
        int addedAngle = negative ? -180 : 0;
        int multipliedBy = negative ? -1 : 1;

        float newYaw = (float) ((addedAngle + ((90 * velocity.x) / (baseAngle) * multipliedBy)));
        if (newYaw < -180) {
            newYaw += 360;
        }

        float newPitch = (float) ((90 * velocity.y) / (baseAngle + Math.abs(velocity.y)));

        // Check collision
        HitResult hitResult = ProjectileUtil.getCollision(this, Entity::canHit);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
        }

        if (!Double.isNaN(newYaw)) {
            this.setYaw(newYaw);
        }
        if (!Double.isNaN(newPitch)) {
            this.setPitch(newPitch);
        }

        this.setPosition(x, y, z);

        if (this.getWorld().isClient) {
            this.playTravelParticle(x, y, z);
        }
    }

    protected void onCollision(HitResult hitResult) {
        switch (hitResult.getType()) {
            case ENTITY -> {
                EntityHitResult entityHitResult = (EntityHitResult)hitResult;
                Entity entity = entityHitResult.getEntity();
                if (entity.getType().isIn(EntityTypeTags.REDIRECTABLE_PROJECTILE) && entity instanceof ProjectileEntity projectileEntity) {
                    projectileEntity.deflect(ProjectileDeflection.REDIRECTED, this.getOwner(), this.getOwner(), true);
                }

                this.onEntityHit(entity);
            }
            case BLOCK -> this.onBlockHit();
        }
    }

    protected void onEntityHit(Entity entity) {
        if (!this.getWorld().isClient) {
            this.playDiscardParticle(this.getX(), this.getY(), this.getZ());
            this.discard();
        }
    }

    protected void onBlockHit() {
        if (!this.getWorld().isClient) {
            this.playDiscardParticle(this.getX(), this.getY(), this.getZ());
            this.discard();
        }
    }

    @Override
    protected void onTimeOut() {
        if (!this.getWorld().isClient) {
            playDiscardParticle(this.getX(), this.getY(), this.getZ());
        }
    }
}
