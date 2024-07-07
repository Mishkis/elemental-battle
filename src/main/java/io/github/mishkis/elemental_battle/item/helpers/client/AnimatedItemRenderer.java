package io.github.mishkis.elemental_battle.item.helpers.client;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.item.helpers.IdleAnimatedItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AnimatedItemRenderer extends GeoItemRenderer<IdleAnimatedItem> {
    public AnimatedItemRenderer(String current_item) {
        super(new DefaultedItemGeoModel<>(Identifier.of(ElementalBattle.MOD_ID, current_item)));
    }
}
