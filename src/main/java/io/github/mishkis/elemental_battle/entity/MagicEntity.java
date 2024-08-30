package io.github.mishkis.elemental_battle.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class MagicEntity extends Entity implements Ownable {
    private static final TrackedData<Integer> UPTIME = DataTracker.registerData(MagicEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> DAMAGE = DataTracker.registerData(MagicEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private PlayerEntity owner;
    private UUID ownerUuid;

    public MagicEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public void setDamage(Float damage) {
        dataTracker.set(DAMAGE, damage);
    }

    public void setUptime(Integer uptime) {
        dataTracker.set(UPTIME, uptime);
    }

    public Float getDamage() {
        return dataTracker.get(DAMAGE);
    }

    public Integer getUptime() {
        return dataTracker.get(UPTIME);
    }

    public void setOwner(PlayerEntity owner) {
        if (owner != null) {
            this.owner = owner;
            this.ownerUuid = owner.getUuid();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getUptime() < age) {
            onTimeOut();
            this.discard();
        }
    }

    protected void onTimeOut() {
    }

    // The entity will be discarded if it is ever is null, however you must still check if it is null as otherwise it could break when joining a world/
    @Nullable
    @Override
    public PlayerEntity getOwner() {
        if (this.owner == null) {
            if (this.ownerUuid != null && this.getWorld() instanceof ServerWorld world) {
                this.owner = world.getPlayerByUuid(ownerUuid);

                if (this.owner == null) {
                    this.discard();
                }
            }
            else {
                this.discard();
            }
        }

        return this.owner;
    }


    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(UPTIME, 0);
        builder.add(DAMAGE, 0f);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        return new EntitySpawnS2CPacket(this, entityTrackerEntry, owner == null ? 0 : owner.getId());
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);

        if (this.getWorld().getEntityById(packet.getEntityData()) instanceof PlayerEntity player) {
            this.setOwner(player);
        }
    }
}
