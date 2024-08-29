package io.github.mishkis.elemental_battle.item.armor;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.item.ElementalBattleItems;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ElementalBattleArmorMaterials {
	public static final RegistryEntry<ArmorMaterial> FROZEN_CLOTH = registerMaterial("frozen_cloth", () -> Ingredient.ofItems(ElementalBattleItems.FROZEN_CLOTH));
	public static final RegistryEntry<ArmorMaterial> BURNING_CLOTH = registerMaterial("burning_cloth", () -> Ingredient.ofItems(ElementalBattleItems.BURNING_CLOTH));
	public static final RegistryEntry<ArmorMaterial> BILLOWING_CLOTH = registerMaterial("billowing_cloth", () -> Ingredient.ofItems(ElementalBattleItems.BILLOWING_CLOTH));

	public static void initialize() {}

	public static RegistryEntry<ArmorMaterial> registerMaterial(String id, Supplier<Ingredient> ingredient) {
		return registerMaterial(id,
				Map.of(
						ArmorItem.Type.BOOTS, 2,
						ArmorItem.Type.LEGGINGS, 5,
						ArmorItem.Type.CHESTPLATE, 6,
						ArmorItem.Type.HELMET, 2
				),15, RegistryEntry.of(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE),  ingredient, 2.0F, 0.0F
		);
	}

	public static RegistryEntry<ArmorMaterial> registerMaterial(String id, Map<ArmorItem.Type, Integer> defensePoints, int enchantability, RegistryEntry<SoundEvent> equipSound, Supplier<Ingredient> repairIngredientSupplier, float toughness, float knockbackResistance) {
		List<ArmorMaterial.Layer> layers = List.of(
				new ArmorMaterial.Layer(Identifier.of(ElementalBattle.MOD_ID, id), "", false)
		);

		ArmorMaterial material = new ArmorMaterial(defensePoints, enchantability, equipSound, repairIngredientSupplier, layers, toughness, knockbackResistance);
		material = Registry.register(Registries.ARMOR_MATERIAL, Identifier.of(ElementalBattle.MOD_ID, id), material);

		return RegistryEntry.of(material);
	}
}