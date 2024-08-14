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

public abstract class MagicDashEntity extends MagicEntity {
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
}
