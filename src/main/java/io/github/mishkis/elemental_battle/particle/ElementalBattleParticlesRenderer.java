package io.github.mishkis.elemental_battle.particle;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.*;

public class ElementalBattleParticlesRenderer {
    public static void initialize() {
        ParticleFactoryRegistry.getInstance().register(ElementalBattleParticles.FROST_SHATTER_PARTICLE, GustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ElementalBattleParticles.SMALL_FROST_SHATTER_PARTICLE, GustParticle.SmallGustFactory::new);
        ParticleFactoryRegistry.getInstance().register(ElementalBattleParticles.FROST_PARTICLE, SnowflakeParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(ElementalBattleParticles.FLAME_PARTICLE_FULL, ExplosionLargeParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ElementalBattleParticles.FLAME_PARTICLE_PARTIAL, SonicBoomParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ElementalBattleParticles.FLAME_PARTICLE_SMOKE, GustParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(ElementalBattleParticles.GUST_PARTICLE, GustParticle.SmallGustFactory::new);
        ParticleFactoryRegistry.getInstance().register(ElementalBattleParticles.GUST_EXPLOSION_PARTICLE, GustParticle.Factory::new);
    }
}
