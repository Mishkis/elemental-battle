package io.github.mishkis.elemental_battle.network;

import io.github.mishkis.elemental_battle.item.helpers.MagicStaffItem;
import io.github.mishkis.elemental_battle.spells.SpellCooldownManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import javax.swing.text.JTextComponent;

public class ElementalBattleNetworkClient {
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
    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (shield.wasPressed()) {
                if (client.player.getStackInHand(client.player.getActiveHand()).getItem() instanceof MagicStaffItem staff) {
                    staff.shield(client.world, client.player, client.player.getActiveHand());
                }
                ClientPlayNetworking.send(new C2SKeybindPayload("shield"));
            }

            while (dash.wasPressed()) {
                if (client.player.getStackInHand(client.player.getActiveHand()).getItem() instanceof MagicStaffItem staff) {
                    staff.dash(client.world, client.player, client.player.getActiveHand());
                }
                ClientPlayNetworking.send(new C2SKeybindPayload("dash"));
            }

            while (areaAttack.wasPressed()) {
                if (client.player.getStackInHand(client.player.getActiveHand()).getItem() instanceof MagicStaffItem staff) {
                    staff.areaAttack(client.world, client.player, client.player.getActiveHand());
                }
                ClientPlayNetworking.send(new C2SKeybindPayload("areaAttack"));
            }

            while (special.wasPressed()) {
                if (client.player.getStackInHand(client.player.getActiveHand()).getItem() instanceof MagicStaffItem staff) {
                    staff.special(client.world, client.player, client.player.getActiveHand());
                }
                ClientPlayNetworking.send(new C2SKeybindPayload("special"));
            }

            while (ultimate.wasPressed()) {
                if (client.player.getStackInHand(client.player.getActiveHand()).getItem() instanceof MagicStaffItem staff) {
                    staff.ultimate(client.world, client.player, client.player.getActiveHand());
                }
                ClientPlayNetworking.send(new C2SKeybindPayload("ultimate"));
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(S2CSpellCooldownManagerRemove.ID, ((payload, context) -> {
            context.client().execute(() -> {
                context.player().getAttached(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT).remove(payload.identifier());
            });
        }));

    }
}
