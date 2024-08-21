package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MagicStaffItemRenderer extends GeoItemRenderer<MagicStaffItem> {
    public MagicStaffItemRenderer(String current_item) {
        super(new DefaultedItemGeoModel<>(Identifier.of(ElementalBattle.MOD_ID, current_item)));
    }
}
