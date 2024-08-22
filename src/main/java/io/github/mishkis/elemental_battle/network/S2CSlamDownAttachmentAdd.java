package io.github.mishkis.elemental_battle.network;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record S2CSlamDownAttachmentAdd(Integer entityId) implements CustomPayload {
    public static final CustomPayload.Id<S2CSlamDownAttachmentAdd> ID = new CustomPayload.Id<>(Identifier.of(ElementalBattle.MOD_ID, "slam_down_attachment_sync_payload"));
    public static final PacketCodec<RegistryByteBuf, S2CSlamDownAttachmentAdd> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, S2CSlamDownAttachmentAdd::entityId,
            S2CSlamDownAttachmentAdd::new
    );


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
