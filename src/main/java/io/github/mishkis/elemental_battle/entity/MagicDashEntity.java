package io.github.mishkis.elemental_battle.entity;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class MagicDashEntity extends Entity implements Ownable {
    private PlayerEntity owner;
    private UUID ownerUuid;

    private Vec3d ownerVelocity;

    public MagicDashEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public abstract float getBlocksTraveled();

    public abstract float getUptime();

    // Can the dash go into the air or not.
    public abstract boolean isGrounded();

    @Environment(EnvType.CLIENT)
    public abstract void playParticle(Vec3d pos);

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

    @Override
    public void tick() {
        super.tick();

        PlayerEntity owner = this.getOwner();
        if (owner != null) {
            if (ownerVelocity == null) {
                ownerVelocity = owner.getRotationVector();
                if (this.isGrounded()) {
                    ownerVelocity = owner.getRotationVector(0, owner.getYaw());
                }
                ownerVelocity = ownerVelocity.multiply(this.getBlocksTraveled()/this.getUptime());

                this.setYaw(-owner.getYaw());

                // Trick the game into preventing fall damage by claiming it's an "explosion".
                owner.currentExplosionImpactPos = owner.getPos();
            }
            owner.setIgnoreFallDamageFromCurrentExplosion(true);

            owner.setVelocity(ownerVelocity);

            this.setPosition(owner.getPos());

            if (this.getWorld().isClient) {
                this.playParticle(owner.getPos());
            }
        }
        else {
            this.discard();
        }

        if (this.getUptime() < age) {
            this.discard();
        }
    }

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
