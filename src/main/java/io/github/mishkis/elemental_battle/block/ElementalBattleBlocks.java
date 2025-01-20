package io.github.mishkis.elemental_battle.block;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ElementalBattleBlocks {
    public static final Block FROZEN_COBBLESTONE = register(new FrozenCobblestoneBlock(AbstractBlock.Settings.create()), "frozen_cobblestone", false);

    public static Block register(Block block, String name, boolean shouldRegisterItem) {
        Identifier id = Identifier.of(ElementalBattle.MOD_ID, name);

        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }

        return Registry.register(Registries.BLOCK, id, block);
    }

    public static void initialize() {}
}
