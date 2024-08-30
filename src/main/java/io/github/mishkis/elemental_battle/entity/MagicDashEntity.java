package io.github.mishkis.elemental_battle.entity;

import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class MagicDashEntity extends MagicEntity {
    private Vec3d ownerVelocity;

    public MagicDashEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public abstract float getBlocksTraveled();

    // Can the dash go into the air or not.
    public abstract boolean isGrounded();

    @Environment(EnvType.CLIENT)
    public abstract void playParticle(Vec3d pos);

    @Override
    public void tick() {
        super.tick();

        if (this.getOwner() instanceof PlayerEntity owner) {
            if (ownerVelocity == null) {
                ownerVelocity = owner.getRotationVector();
                if (this.isGrounded()) {
                    ownerVelocity = owner.getRotationVector(0, owner.getYaw());
                }
                ownerVelocity = ownerVelocity.multiply(this.getBlocksTraveled() / this.getUptime());

                this.setYaw(-owner.getYaw());

                // Trick the game into preventing fall damage by claiming it's an "explosion".
                owner.currentExplosionImpactPos = owner.getPos().subtract(0, 500, 0);
                owner.setIgnoreFallDamageFromCurrentExplosion(true);
            }
            owner.setVelocity(ownerVelocity);
            owner.setStatusEffect(new StatusEffectInstance(ElementalBattleStatusEffects.SPELL_LOCK_EFFECT, 2, 0), this);

            this.setPosition(owner.getPos());

            if (this.getWorld().isClient) {
                this.playParticle(owner.getPos());
            }
        }
    }
}
