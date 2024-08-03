package io.github.mishkis.elemental_battle;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntitiesRenderer;
import io.github.mishkis.elemental_battle.misc.ElementalBattleParticlesRenderer;
import io.github.mishkis.elemental_battle.network.KeybindPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ElementalBattleClient implements ClientModInitializer {
    private static KeyBinding shield = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.elemental_battle.shield",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "keyGroup.elemental_battle"
    ));
    private static KeyBinding dash = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.elemental_battle.dash",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            "keyGroup.elemental_battle"
    ));
    private static KeyBinding areaAttack = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.elemental_battle.area_attack",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "keyGroup.elemental_battle"
    ));
    private static KeyBinding special = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.elemental_battle.special",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "keyGroup.elemental_battle"
    ));
    private static KeyBinding ultimate = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.elemental_battle.ultimate",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "keyGroup.elemental_battle"
    ));

    @Override
    public void onInitializeClient() {
        ElementalBattleEntitiesRenderer.initialize();
        ElementalBattleParticlesRenderer.initialize();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (shield.wasPressed()) {
                ClientPlayNetworking.send(new KeybindPayload("shield"));
            }

            while (dash.wasPressed()) {
                ClientPlayNetworking.send(new KeybindPayload("dash"));
            }

            while (areaAttack.wasPressed()) {
                ClientPlayNetworking.send(new KeybindPayload("area_attack"));
            }

            while (special.wasPressed()) {
                ClientPlayNetworking.send(new KeybindPayload("special"));
            }

            while (ultimate.wasPressed()) {
                ClientPlayNetworking.send(new KeybindPayload("ultimate"));
            }
        });
    }
}
