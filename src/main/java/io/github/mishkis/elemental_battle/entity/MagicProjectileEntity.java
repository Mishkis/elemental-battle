package io.github.mishkis.elemental_battle.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class MagicProjectileEntity extends ProjectileEntity {
    private int uptime = 20 * 20;
    private double gravity = 0.05;
    private float damage;

    // Called on both client and server sides, make sure to check.
    protected abstract void playTravelParticle(double x, double y, double z);

    protected abstract void playDiscardParticle(double x, double y, double z);

    public MagicProjectileEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setUptime(int uptime) {
        this.uptime = uptime;
    }

    public int getUptime() {
        return uptime;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    @Override
    public double getGravity() {
        return this.gravity;
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
        if (this.getUptime() < age && !this.getWorld().isClient) {
            this.playDiscardParticle(x, y, z);
            this.discard();
        }

        // Check collision
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
        }

        this.setYaw(newYaw);
        this.setPitch(newPitch);

        this.setPosition(x, y, z);

        this.playTravelParticle(x, y, z);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        if (!this.getWorld().isClient) {
            this.playDiscardParticle(this.getX(), this.getY(), this.getZ());
            this.discard();
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }
}
