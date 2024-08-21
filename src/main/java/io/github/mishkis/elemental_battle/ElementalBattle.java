package io.github.mishkis.elemental_battle;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.item.ElementalBattleItems;
import io.github.mishkis.elemental_battle.item.helpers.MagicStaffItem;
import io.github.mishkis.elemental_battle.network.ElementalBattleNetworkMain;
import io.github.mishkis.elemental_battle.network.S2CSpellCooldownManagerRemove;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.rendering.SpellDisplay;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import io.github.mishkis.elemental_battle.network.C2SKeybindPayload;
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
        ElementalBattleNetworkMain.initialize();

        SpellDisplay.initialize();
    }
}