package io.github.mishkis.elemental_battle;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.item.ElementalBattleItems;
import io.github.mishkis.elemental_battle.item.helpers.MagicStaffItem;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.rendering.SpellDisplay;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
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
        ElementalBattleStatusEffects.initialize();

        SpellDisplay.initialize();

        PayloadTypeRegistry.playC2S().register(KeybindPayload.ID, KeybindPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(KeybindPayload.ID, ((payload, context) -> {
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