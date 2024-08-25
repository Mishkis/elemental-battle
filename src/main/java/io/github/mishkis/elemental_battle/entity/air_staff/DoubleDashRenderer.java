package io.github.mishkis.elemental_battle.entity.air_staff;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DoubleDashRenderer extends GeoEntityRenderer<DoubleDashEntity> {
    public DoubleDashRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(Identifier.of(ElementalBattle.MOD_ID, "double_dash")));
    }
}
