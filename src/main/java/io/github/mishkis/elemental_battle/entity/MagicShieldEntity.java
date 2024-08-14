package io.github.mishkis.elemental_battle.entity;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class MagicShieldEntity extends MagicEntity {
    private PlayerEntity owner;
    private UUID ownerUuid;
    private float uptime;

    // This is used in the shield effect mixin.
    public static final AttachmentType<MagicShieldEntity> SHIELD_ATTACHMENT = AttachmentRegistry.create(Identifier.of(ElementalBattle.MOD_ID, "shield_attachment"));

    public MagicShieldEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Environment(EnvType.CLIENT)
    public abstract void playParticle(Vec3d pos);

    public void setUptime(float uptime) {
        this.uptime = uptime;
    }

    @Override
    public void tick() {
        super.tick();

        PlayerEntity owner = this.getOwner();
        if (owner != null) {
            if (!this.getWorld().isClient()) {
                shieldEffect(owner);

                if (uptime < age) {
                    onTimeOut(owner);
                    owner.removeAttached(SHIELD_ATTACHMENT);
                    this.discard();
                }
            }

            this.setPosition(owner.getPos());
            if (this.getWorld().isClient) {
                this.playParticle(owner.getPos());
            }
        }
        else {
            this.discard();
        }
    }

    @Override
    public boolean canBeHitByProjectile() {
        return true;
    }

    // Override to create custom on hit effect.
    @Override
    public boolean damage(DamageSource source, float amount) {
        onDamaged(source);
        return false;
    }

    @Override
    public void onDamaged(DamageSource damageSource) {
        super.onDamaged(damageSource);
    }

    // Override to add custom functionality to tick.
    public void shieldEffect(PlayerEntity owner) {
        owner.setStatusEffect(new StatusEffectInstance(ElementalBattleStatusEffects.SHIELD_EFFECT, 10, 0), this);
    }

    // Override to add custom functionality on time out.
    public void onTimeOut(PlayerEntity owner) {}
}