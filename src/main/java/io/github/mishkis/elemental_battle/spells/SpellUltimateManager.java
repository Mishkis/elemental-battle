package io.github.mishkis.elemental_battle.spells;

import com.google.common.collect.Maps;
import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.network.S2C.S2CSpellUltimateManagerAdd;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Map;

public class SpellUltimateManager {
    // Integer is between 0-100.
    private final Map<SpellElement, Integer> ultimateMap = Maps.newHashMap();

    public static final AttachmentType<SpellUltimateManager> SPELL_ULTIMATE_MANAGER_ATTACHMENT = AttachmentRegistry.createDefaulted(Identifier.of(ElementalBattle.MOD_ID, "spell_ultimate_manager_attachment"), SpellUltimateManager::new);

    public Double getPercent(SpellElement element) {
        return ultimateMap.getOrDefault(element, 0) * 0.01;
    }

    public void add(SpellElement element, Integer percent, PlayerEntity owner) {
        if (owner instanceof ServerPlayerEntity player) {
            ServerPlayNetworking.send(player, new S2CSpellUltimateManagerAdd(element, percent));
        }

        percent += ultimateMap.getOrDefault(element, 0);
        if (percent >= 100) {
            ultimateMap.put(element, 100);
        }
        else {
            ultimateMap.put(element, percent);
        }
    }

    public void reset(SpellElement element) {
        ultimateMap.remove(element);
    }
}