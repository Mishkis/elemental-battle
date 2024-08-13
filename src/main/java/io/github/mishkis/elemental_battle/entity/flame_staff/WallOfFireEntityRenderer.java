package io.github.mishkis.elemental_battle.entity.flame_staff;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class WallOfFireEntityRenderer extends GeoEntityRenderer<WallOfFireEntity> {
    public WallOfFireEntityRenderer(EntityRendererFactory.Context renderManager) {
        // Uses the same textures as Flaming Dash.
        super(renderManager, new DefaultedEntityGeoModel(Identifier.of(ElementalBattle.MOD_ID, "flaming_dash")));

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
