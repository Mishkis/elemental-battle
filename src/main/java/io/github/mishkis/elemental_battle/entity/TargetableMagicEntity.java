package io.github.mishkis.elemental_battle.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.world.World;

public abstract class TargetableMagicEntity extends MagicEntity {
    private static final TrackedData<Integer> TARGET_ID = DataTracker.registerData(TargetableMagicEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private Entity target;

    public void setTarget(Entity target) {
        if (target != null) {
            this.target = target;
            dataTracker.set(TARGET_ID, target.getId());
        }
    }

    public Entity getTarget() {
        if (this.target == null) {
            if (dataTracker.get(TARGET_ID) != 0) {
                this.target = this.getWorld().getEntityById(dataTracker.get(TARGET_ID));

                if (this.target == null) {
                    this.discard();
                }
            }
            else {
                this.discard();
            }
        }

        return this.target;
    }

    public TargetableMagicEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(TARGET_ID, 0);
    }
}
