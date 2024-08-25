package io.github.mishkis.elemental_battle.network.S2C;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record S2CSpellCooldownManagerRemove(Identifier identifier) implements CustomPayload {
    public static final CustomPayload.Id<S2CSpellCooldownManagerRemove> ID = new CustomPayload.Id<>(Identifier.of(ElementalBattle.MOD_ID, "spell_cooldown_manager_remove_payload"));
    public static final PacketCodec<RegistryByteBuf, S2CSpellCooldownManagerRemove> CODEC = PacketCodec.tuple(
            Identifier.PACKET_CODEC, S2CSpellCooldownManagerRemove::identifier,
            S2CSpellCooldownManagerRemove::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
