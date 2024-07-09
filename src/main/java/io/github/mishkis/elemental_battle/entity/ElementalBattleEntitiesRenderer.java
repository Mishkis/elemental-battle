package io.github.mishkis.elemental_battle.entity;

import io.github.mishkis.elemental_battle.entity.frost_staff.SpikeBallRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ElementalBattleEntitiesRenderer {
    public static void initialize() {
        EntityRendererRegistry.register(ElementalBattleEntities.SPIKEBALL, SpikeBallRenderer::new);
    }
}
