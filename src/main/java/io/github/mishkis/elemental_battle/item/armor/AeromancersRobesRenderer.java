package io.github.mishkis.elemental_battle.item.armor;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AeromancersRobesRenderer extends GeoArmorRenderer<AeromancersRobesItem> {
    public AeromancersRobesRenderer() {
        super(new DefaultedItemGeoModel<>(Identifier.of(ElementalBattle.MOD_ID, "armor/aeromancers_robes")));
    }
}
