package io.github.mishkis.elemental_battle.particle;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ElementalBattleParticles {
    public static final SimpleParticleType FROST_SHATTER_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType SMALL_FROST_SHATTER_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType FROST_PARTICLE = FabricParticleTypes.simple();

    public static final SimpleParticleType FLAME_PARTICLE_FULL = FabricParticleTypes.simple();
    public static final SimpleParticleType FLAME_PARTICLE_PARTIAL = FabricParticleTypes.simple();
    public static final SimpleParticleType FLAME_PARTICLE_SMOKE = FabricParticleTypes.simple();
    public static final SimpleParticleType FIREBALL = FabricParticleTypes.simple();

    public static final SimpleParticleType GUST_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType GUST_EXPLOSION_PARTICLE = FabricParticleTypes.simple();

    public static void initialize() {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "frost_shatter_particle"), FROST_SHATTER_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "small_frost_shatter_particle"), SMALL_FROST_SHATTER_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "frost_particle"), FROST_PARTICLE);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "flame_particle_full"), FLAME_PARTICLE_FULL);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "flame_particle_partial"), FLAME_PARTICLE_PARTIAL);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "flame_particle_smoke"), FLAME_PARTICLE_SMOKE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "fireball_particle"), FIREBALL);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "gust_particle"), GUST_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "gust_explosion_particle"), GUST_EXPLOSION_PARTICLE);
    }
}
