package io.github.mishkis.elemental_battle.network;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.item.MagicStaffActions;
import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.network.C2S.C2SKeybindPayload;
import io.github.mishkis.elemental_battle.network.S2C.S2CGustEntityEmpoweredSet;
import io.github.mishkis.elemental_battle.network.S2C.S2CSlamDownAttachmentAdd;
import io.github.mishkis.elemental_battle.network.S2C.S2CSpellCooldownManagerRemove;
import io.github.mishkis.elemental_battle.network.S2C.S2CSpellUltimateManagerAdd;
import io.github.mishkis.elemental_battle.spells.SpellCooldownManager;
import io.github.mishkis.elemental_battle.spells.SpellUltimateManager;
import io.github.mishkis.elemental_battle.spells.air.GustSpell;
import io.github.mishkis.elemental_battle.spells.air.SlamDownSpell;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import org.lwjgl.glfw.GLFW;

public class ElementalBattleNetworkClient {
    private static boolean shieldHeld = false;
    private static boolean dashHeld = false;
    private static boolean areaAttackHeld = false;
    private static boolean specialHeld = false;
    private static boolean ultimateHeld = false;

    public static KeyBinding shield = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.elemental_battle.shield",
            GLFW.GLFW_KEY_Z,
            "keyGroup.elemental_battle"
    ));
    public static KeyBinding dash = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.elemental_battle.dash",
            GLFW.GLFW_KEY_LEFT_ALT,
            "keyGroup.elemental_battle"
    ));
    public static KeyBinding areaAttack = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.elemental_battle.area_attack",
            GLFW.GLFW_KEY_R,
            "keyGroup.elemental_battle"
    ));
    public static KeyBinding special = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.elemental_battle.special",
            GLFW.GLFW_KEY_V,
            "keyGroup.elemental_battle"
    ));
    public static KeyBinding ultimate = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.elemental_battle.ultimate",
            GLFW.GLFW_KEY_G,
            "keyGroup.elemental_battle"
    ));

    private static void handleInput(MagicStaffActions type, Boolean released, MinecraftClient client) {
        if (client.player.getStackInHand(client.player.getActiveHand()).getItem() instanceof MagicStaffItem staff) {
            staff.castByType(type, client.world, client.player, client.player.getActiveHand(), released);
            ClientPlayNetworking.send(new C2SKeybindPayload(type, released));
        }
    }

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (shield.isPressed()) {
                shieldHeld = true;
                handleInput(MagicStaffActions.SHIELD, false, client);
            }

            if (shieldHeld && !shield.isPressed()) {
                shieldHeld = false;
                handleInput(MagicStaffActions.SHIELD, true, client);
            }

            if (dash.isPressed()) {
                dashHeld = true;
                handleInput(MagicStaffActions.DASH, false, client);
            }

            if (dashHeld && !dash.isPressed()) {
                dashHeld = false;
                handleInput(MagicStaffActions.DASH, true, client);
            }

            if (areaAttack.isPressed()) {
                areaAttackHeld = true;
                handleInput(MagicStaffActions.AREA_ATTACK, false, client);
            }

            if (areaAttackHeld && !areaAttack.isPressed()) {
                areaAttackHeld = false;
                handleInput(MagicStaffActions.AREA_ATTACK, true, client);
            }

            if (special.isPressed()) {
                areaAttackHeld = true;
                handleInput(MagicStaffActions.SPECIAL, false, client);
            }

            if (specialHeld && !special.isPressed()) {
                specialHeld = false;
                handleInput(MagicStaffActions.SPECIAL, true, client);
            }

            if (ultimate.isPressed()) {
                ultimateHeld = true;
                handleInput(MagicStaffActions.ULTIMATE, false, client);
            }

            if (ultimateHeld && !ultimate.isPressed()) {
                ultimateHeld = false;
                handleInput(MagicStaffActions.ULTIMATE, true, client);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(S2CGustEntityEmpoweredSet.ID, ((payload, context) -> {
            context.client().execute(() -> {
                context.player().setIgnoreFallDamageFromCurrentExplosion(payload.value());
                context.player().setAttached(GustSpell.EMPOWERED_ATTACHMENT, payload.value());
            });
        }));

        ClientPlayNetworking.registerGlobalReceiver(S2CSpellCooldownManagerRemove.ID, ((payload, context) -> {
            context.client().execute(() -> {
                context.player().getAttached(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT).remove(payload.identifier());
            });
        }));

        ClientPlayNetworking.registerGlobalReceiver(S2CSlamDownAttachmentAdd.ID, ((payload, context) -> {
            context.client().execute(() -> {
                Entity entity = context.client().world.getEntityById(payload.entityId());

                if (entity != null) {
                    SlamDownSpell.addToSlamDownList(entity, context.player());
                }
            });
        }));

        ClientPlayNetworking.registerGlobalReceiver(S2CSpellUltimateManagerAdd.ID, ((payload, context) -> {
            context.client().execute(() -> {
                context.player().getAttachedOrCreate(SpellUltimateManager.SPELL_ULTIMATE_MANAGER_ATTACHMENT).add(payload.element(), payload.percent(), context.player());
            });
        }));
    }
}
