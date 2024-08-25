package io.github.mishkis.elemental_battle.network.S2C;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record S2CGustEntityEmpoweredSet(boolean value) implements CustomPayload {
    public static final CustomPayload.Id<S2CGustEntityEmpoweredSet> ID = new CustomPayload.Id<>(Identifier.of(ElementalBattle.MOD_ID, "gust_entity_empowered_set_payload"));
    public static final PacketCodec<RegistryByteBuf, S2CGustEntityEmpoweredSet> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, S2CGustEntityEmpoweredSet::value,
            S2CGustEntityEmpoweredSet::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
