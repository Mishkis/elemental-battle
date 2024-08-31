package io.github.mishkis.elemental_battle.item.armor;

import io.github.mishkis.elemental_battle.rendering.TooltipSpellData;
import io.github.mishkis.elemental_battle.spells.Spell;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Rarity;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;


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
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return Optional.of(new TooltipSpellData(getSpell()));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
