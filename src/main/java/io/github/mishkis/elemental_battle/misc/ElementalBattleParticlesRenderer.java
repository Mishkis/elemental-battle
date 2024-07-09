package io.github.mishkis.elemental_battle.misc;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SnowflakeParticle;

public class ElementalBattleParticlesRenderer {
    public static void initialize() {
        ParticleFactoryRegistry.getInstance().register(ElementalBattleParticles.FROST_PARTICLE, SnowflakeParticle.Factory::new);
    }
}
