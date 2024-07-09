package io.github.mishkis.elemental_battle.misc;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ElementalBattleParticles {
    public static final SimpleParticleType FROST_PARTICLE = FabricParticleTypes.simple();

    public static void initialize() {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "frost_particle"), FROST_PARTICLE);
    }
}
