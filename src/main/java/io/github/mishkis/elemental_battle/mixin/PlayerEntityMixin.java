package io.github.mishkis.elemental_battle.mixin;

import io.github.mishkis.elemental_battle.entity.MagicShieldEntity;
import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.spells.SpellCooldownManager;
import io.github.mishkis.elemental_battle.spells.SpellUltimateManager;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity{
    @Shadow public abstract void increaseStat(Stat<?> stat, int amount);

    @Shadow public abstract float getAttackCooldownProgress(float baseTime);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick()V", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        SpellCooldownManager elementalBattleSpellCooldownManager = this.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT);
        elementalBattleSpellCooldownManager.tick();

        SpellUltimateManager spellUltimateManager = this.getAttachedOrCreate(SpellUltimateManager.SPELL_ULTIMATE_MANAGER_ATTACHMENT);
        spellUltimateManager.tick();

        if (getAttackCooldownProgress(1.0f) == 1 && this.getMainHandStack().getItem() instanceof MagicStaffItem magicStaffItem) {
            magicStaffItem.setEmpoweredStrike();
        }
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