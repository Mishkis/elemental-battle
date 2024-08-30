package io.github.mishkis.elemental_battle.entity;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class MagicShieldEntity extends MagicEntity {
    // This is used in the shield effect mixin to call the ondamaged effect of the shield.
    public static final AttachmentType<MagicShieldEntity> SHIELD_ATTACHMENT = AttachmentRegistry.create(Identifier.of(ElementalBattle.MOD_ID, "shield_attachment"));

    @Override
    public void setOwner(PlayerEntity owner) {
        super.setOwner(owner);

        // Tell owner that a shield is attached.
        if (this.getOwner() != null) {
            this.getOwner().setAttached(SHIELD_ATTACHMENT, this);
        }
    }

    public MagicShieldEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Environment(EnvType.CLIENT)
    public abstract void playParticle(Vec3d pos);

    @Override
    public void tick() {
        super.tick();

        if (this.getOwner() instanceof PlayerEntity owner) {
            if (!this.getWorld().isClient()) {
                shieldEffect(owner);

            }

            this.setPosition(owner.getPos());
            if (this.getWorld().isClient) {
                this.playParticle(owner.getPos());
            }
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
        owner.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 10, 0), this);
        owner.setStatusEffect(new StatusEffectInstance(ElementalBattleStatusEffects.SPELL_LOCK_EFFECT, 10, 0), this);
        owner.setStatusEffect(new StatusEffectInstance(ElementalBattleStatusEffects.SHIELD_EFFECT, 10, 0), this);
    }

    @Override
    protected void onTimeOut() {
        if (this.getOwner() != null) {
            this.getOwner().removeAttached(SHIELD_ATTACHMENT);
        }
    }
}