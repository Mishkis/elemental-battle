package io.github.mishkis.elemental_battle;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntitiesRenderer;
import io.github.mishkis.elemental_battle.misc.ElementalBattleParticlesRenderer;
import net.fabricmc.api.ClientModInitializer;

public class ElementalBattleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ElementalBattleEntitiesRenderer.initialize();
        ElementalBattleParticlesRenderer.initialize();
    }
}
