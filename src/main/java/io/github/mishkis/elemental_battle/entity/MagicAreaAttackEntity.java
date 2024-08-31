package io.github.mishkis.elemental_battle.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;

public abstract class MagicAreaAttackEntity extends MagicEntity {
    private final ArrayList<LivingEntity> hit = new ArrayList<>();

    public MagicAreaAttackEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    protected double getOffset() {
        return 0.8;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getOwner() != null) {
            this.setPosition(this.getOwner().getPos().offset(Direction.UP, getOffset()));

            if (this.getWorld() instanceof ServerWorld serverWorld) {
                for (Entity entity : serverWorld.getOtherEntities(this.getOwner(), this.getBoundingBox(), entity ->
                        entity instanceof LivingEntity livingEntity && !hit.contains(livingEntity)
                )) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    hit.add(livingEntity);
                    onEntityCollision(livingEntity);
                }
            }
        }
    }

    protected abstract void onEntityCollision(LivingEntity entity);
}
