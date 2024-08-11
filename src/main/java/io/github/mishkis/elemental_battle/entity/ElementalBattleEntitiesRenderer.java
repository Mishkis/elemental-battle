package io.github.mishkis.elemental_battle.entity;

import io.github.mishkis.elemental_battle.entity.flame_staff.ConeOfFireRenderer;
import io.github.mishkis.elemental_battle.entity.frost_staff.IceTargetRenderer;
import io.github.mishkis.elemental_battle.entity.frost_staff.IcicleBallRenderer;
import io.github.mishkis.elemental_battle.entity.frost_staff.IcicleRenderer;
import io.github.mishkis.elemental_battle.entity.frost_staff.ShatteringWallRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ElementalBattleEntitiesRenderer {
    public static void initialize() {
        // Frost Magic
        EntityRendererRegistry.register(ElementalBattleEntities.ICE_TARGET, IceTargetRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.ICICLE_BALL, IcicleBallRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.ICICLE, IcicleRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.SHATTERING_WALL, ShatteringWallRenderer::new);

        // Flame Magic
        EntityRendererRegistry.register(ElementalBattleEntities.CONE_OF_FIRE, ConeOfFireRenderer::new);
    }
}
