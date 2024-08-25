package io.github.mishkis.elemental_battle;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntitiesRenderer;
import io.github.mishkis.elemental_battle.network.ElementalBattleNetworkClient;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticlesRenderer;
import io.github.mishkis.elemental_battle.rendering.SpellDisplay;
import net.fabricmc.api.ClientModInitializer;

public class ElementalBattleClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ElementalBattleEntitiesRenderer.initialize();
        ElementalBattleParticlesRenderer.initialize();
        ElementalBattleNetworkClient.initialize();

        SpellDisplay.initialize();
    }
}
