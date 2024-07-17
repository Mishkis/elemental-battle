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

        this.setVelocity(0.1, 0.3, 0.2);

        Vec3d velocity = this.getVelocity();
        double x = this.getX() + velocity.x;
        double y = this.getY() + velocity.y;
        double z = this.getZ() + velocity.z;

        this.setYaw((float) ((90 * velocity.x)/(velocity.x + velocity.y + velocity.z)));
        this.setPitch((float) ((90 * velocity.y)/(velocity.x + velocity.y + velocity.z)));

        this.setPosition(x, y, z);
    }
}
