package io.github.mishkis.elemental_battle;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntitiesRenderer;
import io.github.mishkis.elemental_battle.item.helpers.MagicStaffItem;
import io.github.mishkis.elemental_battle.network.ElementalBattleNetworkClient;
import io.github.mishkis.elemental_battle.network.ElementalBattleNetworkMain;
import io.github.mishkis.elemental_battle.network.S2CSpellCooldownManagerRemove;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticlesRenderer;
import io.github.mishkis.elemental_battle.network.C2SKeybindPayload;
import io.github.mishkis.elemental_battle.spells.SpellCooldownManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ElementalBattleClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ElementalBattleEntitiesRenderer.initialize();
        ElementalBattleParticlesRenderer.initialize();
        ElementalBattleNetworkClient.initialize();
    }
}
