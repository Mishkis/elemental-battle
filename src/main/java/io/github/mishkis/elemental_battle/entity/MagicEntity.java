package io.github.mishkis.elemental_battle.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public abstract class MagicEntity extends Entity implements Ownable {
    private static final TrackedData<Integer> UPTIME = DataTracker.registerData(MagicEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> DAMAGE = DataTracker.registerData(MagicEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(MagicEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private PlayerEntity owner;

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

            dataTracker.set(OWNER_UUID, Optional.of(owner.getUuid()));
        }
    }

    @Override
    public void tick() {
        if (this.getOwner() instanceof PlayerEntity player && player.isRemoved()) {
            this.discard();
        }

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
            if (dataTracker.get(OWNER_UUID).isPresent()) {
                this.owner = this.getWorld().getPlayerByUuid(dataTracker.get(OWNER_UUID).get());

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
        builder.add(DAMAGE, 0f);
        builder.add(UPTIME, 0);
        builder.add(OWNER_UUID, Optional.empty());
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (dataTracker.get(OWNER_UUID).isPresent()) {
            nbt.putUuid("Owner", dataTracker.get(OWNER_UUID).get());
        }

        nbt.putFloat("Damage", dataTracker.get(DAMAGE));
        nbt.putInt("Uptime", dataTracker.get(UPTIME));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) {
            this.setOwner(this.getWorld().getPlayerByUuid(nbt.getUuid("Owner")));
        }
        else {
            this.setOwner(this.getWorld().getPlayerByUuid(ServerConfigHandler.getPlayerUuidByName(this.getServer(), nbt.getString("Owner"))));
        }

        this.setDamage(nbt.getFloat("Damage"));
        this.setUptime(nbt.getInt("Uptime"));
    }
}
