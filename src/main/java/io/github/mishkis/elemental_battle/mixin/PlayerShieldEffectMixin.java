package io.github.mishkis.elemental_battle.mixin;

import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerShieldEffectMixin extends LivingEntity{
    protected PlayerShieldEffectMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
    public void cancelDamageOnShield(CallbackInfoReturnable<Boolean> cir) {
        if (this.getStatusEffect(ElementalBattleStatusEffects.SHIELD_EFFECT) != null) {
            cir.setReturnValue(false);
        }
    }
}
