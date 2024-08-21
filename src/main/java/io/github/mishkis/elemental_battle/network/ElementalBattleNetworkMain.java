package io.github.mishkis.elemental_battle.network;

import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;

public class ElementalBattleNetworkMain {
    public static void initialize() {
        PayloadTypeRegistry.playC2S().register(C2SKeybindPayload.ID, C2SKeybindPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(S2CSpellCooldownManagerRemove.ID, S2CSpellCooldownManagerRemove.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(C2SKeybindPayload.ID, ((payload, context) -> {
            context.server().execute(() -> {
                PlayerEntity player = context.player();
                Hand hand = player.getActiveHand();
                Item heldItem = player.getStackInHand(hand).getItem();

                if (heldItem instanceof MagicStaffItem staff) {
                    switch (payload.type()) {
                        case "shield":
                            staff.shield(player.getWorld(), player, hand);
                            break;
                        case "dash":
                            staff.dash(player.getWorld(), player, hand);
                            break;
                        case "areaAttack":
                            staff.areaAttack(player.getWorld(), player, hand);
                            break;
                        case "special":
                            staff.special(player.getWorld(), player, hand);
                            break;
                        case "ultimate":
                            staff.ultimate(player.getWorld(), player, hand);
                            break;
                    }
                }
            });
        }));
    }
}
