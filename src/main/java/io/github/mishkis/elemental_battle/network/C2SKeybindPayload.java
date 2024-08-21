package io.github.mishkis.elemental_battle.network;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record C2SKeybindPayload(String type) implements CustomPayload {
    public static final CustomPayload.Id<C2SKeybindPayload> ID = new CustomPayload.Id<>(Identifier.of(ElementalBattle.MOD_ID, "keybind_payload"));
    public static final PacketCodec<RegistryByteBuf, C2SKeybindPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, C2SKeybindPayload::type,
            C2SKeybindPayload::new
    );


    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
