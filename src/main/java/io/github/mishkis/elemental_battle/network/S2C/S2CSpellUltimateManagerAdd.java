package io.github.mishkis.elemental_battle.network.S2C;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record S2CSpellUltimateManagerAdd(SpellElement element, Integer percent) implements CustomPayload {
    public static final CustomPayload.Id<S2CSpellUltimateManagerAdd> ID = new CustomPayload.Id<>(Identifier.of(ElementalBattle.MOD_ID, "spell_ultimate_manager_add_payload"));
    public static final PacketCodec<RegistryByteBuf, S2CSpellUltimateManagerAdd> CODEC = PacketCodec.tuple(
            SpellElement.PACKET_CODEC, S2CSpellUltimateManagerAdd::element,
            PacketCodecs.INTEGER, S2CSpellUltimateManagerAdd::percent,
            S2CSpellUltimateManagerAdd::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
