package io.github.mishkis.elemental_battle.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class MagicProjectileEntity extends ProjectileEntity {
    public MagicProjectileEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();

        this.setVelocity(this.getVelocity().subtract(0, 0.05, 0));

        Vec3d velocity = this.getVelocity();
        double x = this.getX() + velocity.x;
        double y = this.getY() + velocity.y;
        double z = this.getZ() + velocity.z;

        double baseAngle = Math.abs(velocity.x) + Math.abs(velocity.z);
        boolean negative = velocity.z < 0;
        int addedAngle = negative ? -180 : 0;
        int multipliedBy = negative ? -1 : 1;

        float yaw = (float) ((addedAngle + ((90 * velocity.x)/(baseAngle) * multipliedBy)));
        if (yaw < -180) {
            yaw += 360;
        }

        this.setYaw(yaw);
        this.setPitch((float) ((90 * velocity.y)/(baseAngle + Math.abs(velocity.y))));

        this.setPosition(x, y, z);
    }
}
