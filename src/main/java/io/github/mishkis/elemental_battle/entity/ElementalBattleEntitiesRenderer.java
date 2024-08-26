package io.github.mishkis.elemental_battle.entity;

import io.github.mishkis.elemental_battle.entity.air_staff.AirLiftRenderer;
import io.github.mishkis.elemental_battle.entity.air_staff.BlowBackRenderer;
import io.github.mishkis.elemental_battle.entity.air_staff.DoubleDashRenderer;
import io.github.mishkis.elemental_battle.entity.air_staff.GustRenderer;
import io.github.mishkis.elemental_battle.entity.flame_staff.*;
import io.github.mishkis.elemental_battle.entity.frost_staff.*;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ElementalBattleEntitiesRenderer {
    public static void initialize() {
        // Frost Magic
        EntityRendererRegistry.register(ElementalBattleEntities.ICE_TARGET, IceTargetRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.ICICLE_BALL, IcicleBallRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.ICICLE, IcicleRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.SHATTERING_WALL, ShatteringWallRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.FROZEN_SLIDE, FrozenSlideRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.CHILLING_AIR, ChillingAirRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.FROZEN_SOLID, FrozenSolidRenderer::new);

        // Flame Magic
        EntityRendererRegistry.register(ElementalBattleEntities.CONE_OF_FIRE, ConeOfFireRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.WALL_OF_FIRE, WallOfFireEntityRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.FLAMING_DASH, FlamingDashRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.FLAME_VORTEX, FlameVortexRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.FIREBALL, FireballRenderer::new);

        // Air Magic
        EntityRendererRegistry.register(ElementalBattleEntities.GUST, GustRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.BLOW_BACK, BlowBackRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.DOUBLE_DASH, DoubleDashRenderer::new);
        EntityRendererRegistry.register(ElementalBattleEntities.AIR_LIFT, AirLiftRenderer::new);
    }
}
