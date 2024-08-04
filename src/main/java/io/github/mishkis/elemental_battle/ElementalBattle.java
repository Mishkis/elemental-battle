package io.github.mishkis.elemental_battle;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.item.ElementalBattleItems;
import io.github.mishkis.elemental_battle.item.helpers.MagicWandItem;
import io.github.mishkis.elemental_battle.misc.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.network.KeybindPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ElementalBattle implements ModInitializer {
    public static final String MOD_ID = "elemental_battle";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Starting Elemental Battle.");
        ElementalBattleItems.initialize();
        ElementalBattleEntities.initialize();
        ElementalBattleParticles.initialize();

        PayloadTypeRegistry.playC2S().register(KeybindPayload.ID, KeybindPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(KeybindPayload.ID, ((payload, context) -> {
            context.server().execute(() -> {
                PlayerEntity player = context.player();
                Hand hand = player.getActiveHand();
                Item heldItem = player.getStackInHand(hand).getItem();

                if (heldItem.getClass().getSuperclass().equals(MagicWandItem.class)) {
                    switch (payload.type()) {
                        case "shield":
                            ((MagicWandItem) heldItem).shield(player.getWorld(), player, hand);
                            break;
                        case "dash":
                            ((MagicWandItem) heldItem).dash(player.getWorld(), player, hand);
                            break;
                        case "areaAttack":
                            ((MagicWandItem) heldItem).areaAttack(player.getWorld(), player, hand);
                            break;
                        case "special":
                            ((MagicWandItem) heldItem).special(player.getWorld(), player, hand);
                            break;
                        case "ultimate":
                            ((MagicWandItem) heldItem).ultimate(player.getWorld(), player, hand);
                            break;
                    }
                }
            });
        }));
    }
}