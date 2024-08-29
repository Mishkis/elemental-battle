package io.github.mishkis.elemental_battle.item.armor;

import io.github.mishkis.elemental_battle.spells.Spell;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;


public abstract class MagicArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Spell spell;

    public MagicArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Spell spell) {
        super(material, type, new Settings().maxCount(1).rarity(Rarity.RARE));

        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("Unlocks: " + spell.getSpellName() + "Spell.").formatted(Formatting.BOLD).formatted(getSpell().getElement().getColor()));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
