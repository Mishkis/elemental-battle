package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.item.armor.AeromancersRobesItem;
import io.github.mishkis.elemental_battle.item.armor.CryomancersRobesItem;
import io.github.mishkis.elemental_battle.item.armor.PyromancersRobesItem;
import io.github.mishkis.elemental_battle.item.element_staffs.AirStaff;
import io.github.mishkis.elemental_battle.item.element_staffs.FlameStaff;
import io.github.mishkis.elemental_battle.item.element_staffs.FrostStaff;
import io.github.mishkis.elemental_battle.spells.air.AirLiftSpell;
import io.github.mishkis.elemental_battle.spells.air.BlowBackSpell;
import io.github.mishkis.elemental_battle.spells.air.DoubleDashSpell;
import io.github.mishkis.elemental_battle.spells.air.SlamDownSpell;
import io.github.mishkis.elemental_battle.spells.flame.FireballSpell;
import io.github.mishkis.elemental_battle.spells.flame.FlameVortexSpell;
import io.github.mishkis.elemental_battle.spells.flame.FlamingDashSpell;
import io.github.mishkis.elemental_battle.spells.flame.WallOfFireSpell;
import io.github.mishkis.elemental_battle.spells.frost.ChillingAirSpell;
import io.github.mishkis.elemental_battle.spells.frost.FrigidGlareSpell;
import io.github.mishkis.elemental_battle.spells.frost.FrozenSlideSpell;
import io.github.mishkis.elemental_battle.spells.frost.ShatteringWallSpell;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArmorItem;
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

    public static final Item FROZEN_CLOTH = register(new Item(new Item.Settings()), "frozen_cloth");
    public static final Item CRYOMANCERS_BOOTS = register(new CryomancersRobesItem(new FrozenSlideSpell(), ArmorItem.Type.BOOTS), "cryomancers_boots");
    public static final Item CRYOMANCERS_LEGGINGS = register(new CryomancersRobesItem(new ChillingAirSpell(), ArmorItem.Type.LEGGINGS), "cryomancers_leggings");
    public static final Item CRYOMANCERS_ROBES = register(new CryomancersRobesItem(new ShatteringWallSpell(), ArmorItem.Type.CHESTPLATE), "cryomancers_robes");
    public static final Item CRYOMANCERS_HOOD = register(new CryomancersRobesItem(new FrigidGlareSpell(), ArmorItem.Type.HELMET), "cryomancers_hood");

    public static final Item BURNING_CLOTH = register(new Item(new Item.Settings()), "burning_cloth");
    public static final Item PYROMANCERS_BOOTS = register(new PyromancersRobesItem(new FlamingDashSpell(), ArmorItem.Type.BOOTS), "pyromancers_boots");
    public static final Item PYROMANCERS_LEGGINGS = register(new PyromancersRobesItem(new FlameVortexSpell(), ArmorItem.Type.LEGGINGS), "pyromancers_leggings");
    public static final Item PYROMANCERS_ROBES = register(new PyromancersRobesItem(new WallOfFireSpell(), ArmorItem.Type.CHESTPLATE), "pyromancers_robes");
    public static final Item PYROMANCERS_HAT = register(new PyromancersRobesItem(new FireballSpell(), ArmorItem.Type.HELMET), "pyromancers_hat");

    public static final Item BILLOWING_CLOTH = register(new Item(new Item.Settings()), "billowing_cloth");
    public static final Item AEROMANCERS_BOOTS = register(new AeromancersRobesItem(new DoubleDashSpell(), ArmorItem.Type.BOOTS), "aeromancers_boots");
    public static final Item AEROMANCERS_LEGGINGS = register(new AeromancersRobesItem(new AirLiftSpell(), ArmorItem.Type.LEGGINGS), "aeromancers_leggings");
    public static final Item AEROMANCERS_ROBES = register(new AeromancersRobesItem(new BlowBackSpell(), ArmorItem.Type.CHESTPLATE), "aeromancers_robes");
    public static final Item AEROMANCERS_CIRCLET = register(new AeromancersRobesItem(new SlamDownSpell(), ArmorItem.Type.HELMET), "aeromancers_circlet");

    public static final RegistryKey<ItemGroup> ELEMENTAL_BATTLE_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(ElementalBattle.MOD_ID, "item_group"));
    public static final ItemGroup ELEMENTAL_BATTLE_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(FROST_STAFF))
            .displayName(Text.translatable("itemGroup.elemental_battle"))
            .build();

    public static Item register(Item item, String id) {
        Identifier itemID = Identifier.of(ElementalBattle.MOD_ID, id);

        return Registry.register(Registries.ITEM, itemID, item);
    }

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, ELEMENTAL_BATTLE_ITEM_GROUP_KEY, ELEMENTAL_BATTLE_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(ELEMENTAL_BATTLE_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(FROST_STAFF);
            itemGroup.add(FLAME_STAFF);
            itemGroup.add(AIR_STAFF);

            itemGroup.add(FROZEN_CLOTH);
            itemGroup.add(BURNING_CLOTH);
            itemGroup.add(BILLOWING_CLOTH);

            itemGroup.add(CRYOMANCERS_HOOD);
            itemGroup.add(CRYOMANCERS_ROBES);
            itemGroup.add(CRYOMANCERS_LEGGINGS);
            itemGroup.add(CRYOMANCERS_BOOTS);

            itemGroup.add(PYROMANCERS_HAT);
            itemGroup.add(PYROMANCERS_ROBES);
            itemGroup.add(PYROMANCERS_LEGGINGS);
            itemGroup.add(PYROMANCERS_BOOTS);

            itemGroup.add(AEROMANCERS_CIRCLET);
            itemGroup.add(AEROMANCERS_ROBES);
            itemGroup.add(AEROMANCERS_LEGGINGS);
            itemGroup.add(AEROMANCERS_BOOTS);
        });
    }
}
