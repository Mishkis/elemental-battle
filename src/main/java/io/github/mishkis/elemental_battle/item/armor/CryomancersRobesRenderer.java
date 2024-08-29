package io.github.mishkis.elemental_battle.item.armor;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class CryomancersRobesRenderer extends GeoArmorRenderer<CryomancersRobesItem> {
    public CryomancersRobesRenderer() {
        super(new DefaultedItemGeoModel<>(Identifier.of(ElementalBattle.MOD_ID, "armor/cryomancers_robes")));
    }
}
