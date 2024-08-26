package io.github.mishkis.elemental_battle.status_effects;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ElementalBattleStatusEffects {
    public static final RegistryEntry<StatusEffect> SHIELD_EFFECT = register("shield", new ShieldEffect());
    public static final RegistryEntry<StatusEffect> SUCCESSFUL_PARRY_EFFECT = register("successful_parry", new SuccessfulParryEffect());
    public static final RegistryEntry<StatusEffect> SPELL_LOCK_EFFECT = register("spell_lock", new SpellLockEffect());

    private static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(ElementalBattle.MOD_ID, id), statusEffect);
    }

    public static void initialize() {}
}
