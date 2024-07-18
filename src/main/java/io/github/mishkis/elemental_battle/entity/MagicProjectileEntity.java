package io.github.mishkis.elemental_battle.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class MagicProjectileEntity extends ProjectileEntity {
    private float damage;

    protected abstract void playTravelParticle(double x, double y, double z);

    public MagicProjectileEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    @Override
    public void tick() {
        super.tick();

        // Check collision
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
        }

        // Move projectile
        this.setVelocity(this.getVelocity().subtract(0, 0.05, 0));

        Vec3d velocity = this.getVelocity();
        double x = this.getX() + velocity.x;
        double y = this.getY() + velocity.y;
        double z = this.getZ() + velocity.z;

        double baseAngle = Math.abs(velocity.x) + Math.abs(velocity.z);
        boolean negative = velocity.z < 0;
        int addedAngle = negative ? -180 : 0;
        int multipliedBy = negative ? -1 : 1;

        float newYaw = (float) ((addedAngle + ((90 * velocity.x)/(baseAngle) * multipliedBy)));
        if (newYaw < -180) {
            newYaw += 360;
        }

        this.setYaw(newYaw);
        this.setPitch((float) ((90 * velocity.y)/(baseAngle + Math.abs(velocity.y))));

        this.setPosition(x, y, z);

        if (this.getWorld().isClient) {
            this.playTravelParticle(x, y, z);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        if (!this.getWorld().isClient) {
            this.discard();
        }
    }
}
