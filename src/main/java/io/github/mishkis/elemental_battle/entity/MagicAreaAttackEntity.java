package io.github.mishkis.elemental_battle.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public abstract class MagicAreaAttackEntity extends Entity implements Ownable {
    private PlayerEntity owner;
    private UUID ownerUuid;

    private ArrayList<LivingEntity> hit = new ArrayList<LivingEntity>();

    public MagicAreaAttackEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public void setOwner(PlayerEntity owner) {
        if (owner != null) {
            this.owner = owner;
            this.ownerUuid = owner.getUuid();
        }
    }

    @Nullable
    @Override
    public PlayerEntity getOwner() {
        if (this.owner != null) {
            return this.owner;
        }
        else {
            if (this.ownerUuid != null && this.getWorld() instanceof ServerWorld world) {
                this.owner = world.getPlayerByUuid(ownerUuid);
                return this.owner;
            }

            return null;
        }
    }

    protected abstract int getUptime();

    @Override
    public void tick() {
        super.tick();

        if (this.getOwner() == null || getUptime() < age) {
            this.discard();
        }
        else {
            this.setPosition(this.getOwner().getPos().offset(Direction.UP, 0.8));

            if (this.getWorld() instanceof ServerWorld serverWorld) {
                for (Entity entity : serverWorld.getOtherEntities(this.getOwner(), this.getBoundingBox(), entity -> {
                    return entity instanceof LivingEntity livingEntity && !hit.contains(livingEntity);
                })) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    hit.add(livingEntity);
                    onEntityCollision(livingEntity);
                }
            }

        }
    }

    protected abstract void onEntityCollision(LivingEntity entity);

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

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

        PlayerEntity owner = (PlayerEntity) this.getWorld().getEntityById(packet.getEntityData());
        if (owner != null) {
            this.setOwner(owner);
        }
    }
}
