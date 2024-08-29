package io.github.mishkis.elemental_battle;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.item.ElementalBattleItems;
import io.github.mishkis.elemental_battle.item.armor.ElementalBattleArmorMaterials;
import io.github.mishkis.elemental_battle.network.ElementalBattleNetworkMain;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ElementalBattle implements ModInitializer {
    public static final String MOD_ID = "elemental_battle";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Starting Elemental Battle.");
        ElementalBattleItems.initialize();
        ElementalBattleArmorMaterials.initialize();

        ElementalBattleEntities.initialize();
        ElementalBattleParticles.initialize();
        ElementalBattleStatusEffects.initialize();

        ElementalBattleNetworkMain.initialize();
    }
}