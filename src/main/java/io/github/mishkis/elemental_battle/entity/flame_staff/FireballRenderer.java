package io.github.mishkis.elemental_battle.entity.flame_staff;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class FireballRenderer extends GeoEntityRenderer<FireballEntity> {
    public FireballRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(Identifier.of(ElementalBattle.MOD_ID, "fireball")));

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
