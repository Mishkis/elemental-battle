package io.github.mishkis.elemental_battle.network.C2S;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.item.MagicStaffActions;
import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record C2SKeybindPayload(MagicStaffActions type, Boolean released) implements CustomPayload {
    public static final CustomPayload.Id<C2SKeybindPayload> ID = new CustomPayload.Id<>(Identifier.of(ElementalBattle.MOD_ID, "keybind_payload"));
    public static final PacketCodec<RegistryByteBuf, C2SKeybindPayload> CODEC = PacketCodec.tuple(
            MagicStaffActions.PACKET_CODEC, C2SKeybindPayload::type,
            PacketCodecs.BOOL, C2SKeybindPayload::released,
            C2SKeybindPayload::new
    );


    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
