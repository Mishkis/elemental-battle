package io.github.mishkis.elemental_battle.particle;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class ElementalBattleParticles {
    public static final SimpleParticleType FROST_SHATTER_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType FROST_PARTICLE = FabricParticleTypes.simple();

    public static final SimpleParticleType FLAME_PARTICLE_FULL = FabricParticleTypes.simple();
    public static final SimpleParticleType FLAME_PARTICLE_PARTIAL = FabricParticleTypes.simple();
    public static final SimpleParticleType FLAME_PARTICLE_SMOKE = FabricParticleTypes.simple();

    public static void initialize() {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "frost_shatter_particle"), FROST_SHATTER_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "frost_particle"), FROST_PARTICLE);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "flame_particle_full"), FLAME_PARTICLE_FULL);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "flame_particle_partial"), FLAME_PARTICLE_PARTIAL);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ElementalBattle.MOD_ID, "flame_particle_smoke"), FLAME_PARTICLE_SMOKE);
    }
}
