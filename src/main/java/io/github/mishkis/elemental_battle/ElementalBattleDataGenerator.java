package io.github.mishkis.elemental_battle;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntitiesTags;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ElementalBattleDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.createPack().addProvider(ElementalBattleEntitiesTags::new);
    }
}
