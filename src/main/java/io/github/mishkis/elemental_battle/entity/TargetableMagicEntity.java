package io.github.mishkis.elemental_battle.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.world.World;

import java.util.UUID;

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

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putInt("Target", dataTracker.get(TARGET_ID));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("Target", NbtElement.INT_TYPE)) {
            this.setTarget(this.getWorld().getEntityById(nbt.getInt("Target")));
        }
        else {
            UUID playerUUID = ServerConfigHandler.getPlayerUuidByName(this.getServer(), nbt.getString("Target"));
            if (playerUUID != null) {
                this.setTarget(this.getWorld().getPlayerByUuid(playerUUID));
            }
        }
    }
}
