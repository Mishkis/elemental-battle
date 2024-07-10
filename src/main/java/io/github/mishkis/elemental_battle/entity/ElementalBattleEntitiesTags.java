package io.github.mishkis.elemental_battle.entity;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class ElementalBattleEntitiesTags extends FabricTagProvider.EntityTypeTagProvider {
    public ElementalBattleEntitiesTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(EntityTypeTags.REDIRECTABLE_PROJECTILE).add(ElementalBattleEntities.ICICLE_BALL);
    }
}