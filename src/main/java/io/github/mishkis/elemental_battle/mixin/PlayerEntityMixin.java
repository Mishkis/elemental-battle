package io.github.mishkis.elemental_battle.mixin;

import io.github.mishkis.elemental_battle.entity.MagicShieldEntity;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity{
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
    public void cancelDamageOnShield(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.getStatusEffect(ElementalBattleStatusEffects.SHIELD_EFFECT) != null) {
            // Call the on damaged call on the current Elemental Battle activated shield.
            MagicShieldEntity magicShield = this.getAttached(MagicShieldEntity.SHIELD_ATTACHMENT);

            if (magicShield != null) {
                magicShield.onDamaged(source);
            }

            cir.setReturnValue(false);
        }
    }
}