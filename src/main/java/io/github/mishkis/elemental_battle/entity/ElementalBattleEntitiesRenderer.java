package io.github.mishkis.elemental_battle.entity;

import io.github.mishkis.elemental_battle.entity.flame_staff.ConeOfFireRenderer;
import io.github.mishkis.elemental_battle.entity.frost_staff.IcicleBallRenderer;
import io.github.mishkis.elemental_battle.entity.frost_staff.IcicleRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ElementalBattleEntitiesRenderer {
    public static void initialize() {
        EntityRendererRegistry.register(ElementalBattleEntities.ICICLE_BALL, IcicleBallRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.ICICLE, IcicleRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.CONE_OF_FIRE, ConeOfFireRenderer::new);
    }
}
