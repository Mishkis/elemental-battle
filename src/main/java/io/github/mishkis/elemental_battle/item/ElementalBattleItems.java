package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.item.element_staffs.AirStaff;
import io.github.mishkis.elemental_battle.item.element_staffs.FlameStaff;
import io.github.mishkis.elemental_battle.item.element_staffs.FrostStaff;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ElementalBattleItems {
    public static final Item FROST_STAFF = register(new FrostStaff(), "frost_staff");
    public static final Item FLAME_STAFF = register(new FlameStaff(), "flame_staff");
    public static final Item AIR_STAFF = register(new AirStaff(), "air_staff");

    public static final RegistryKey<ItemGroup> TESTING_MOD_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(ElementalBattle.MOD_ID, "item_group"));
    public static final ItemGroup TESTING_MOD_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(FROST_STAFF))
            .displayName(Text.translatable("itemGroup.elemental_battle"))
            .build();

    public static Item register(Item item, String id) {
        Identifier itemID = Identifier.of(ElementalBattle.MOD_ID, id);

        return Registry.register(Registries.ITEM, itemID, item);
    }

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, TESTING_MOD_ITEM_GROUP_KEY, TESTING_MOD_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(TESTING_MOD_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(FROST_STAFF);
            itemGroup.add(FLAME_STAFF);
            itemGroup.add(AIR_STAFF);
        });
    }
}
