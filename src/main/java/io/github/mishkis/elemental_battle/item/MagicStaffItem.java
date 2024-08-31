package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.item.armor.MagicArmorItem;
import io.github.mishkis.elemental_battle.rendering.TooltipSpellData;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class MagicStaffItem extends Item implements GeoItem {
    private final String id;
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public MagicStaffItem(String id) {
        super(new Settings().maxCount(1).rarity(Rarity.EPIC));

        this.id = id;

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return Optional.of(new TooltipSpellData(useSpell()));
    }

    protected abstract SpellElement getElement();

    @Nullable
    protected abstract Spell useSpell();

    @Nullable
    protected abstract Spell ultimateSpell();

    @Nullable
    private Spell compareArmor(PlayerEntity user, Integer slot) {
        if (user.getInventory().getArmorStack(slot).getItem() instanceof MagicArmorItem magicArmorItem && magicArmorItem.getSpell().getElement() == this.getElement()) {
            return magicArmorItem.getSpell();
        }

        return null;
    }

    @Nullable
    public Spell getUseSpell(PlayerEntity user) {
        return useSpell();
    }

    @Nullable
    public Spell getShieldSpell(PlayerEntity user) {
        // Chestplate
        return compareArmor(user, 2);
    }

    @Nullable
    public Spell getDashSpell(PlayerEntity user) {
        // Boots
        return compareArmor(user, 0);
    }

    @Nullable
    public Spell getAreaAttackSpell(PlayerEntity user) {
        // Leggings
        return compareArmor(user, 1);
    }

    @Nullable
    public Spell getSpecialSpell(PlayerEntity user) {
        // Helmet
        return compareArmor(user, 3);
    }

    @Nullable
    public Spell getUltimateSpell(PlayerEntity user) {
        return ultimateSpell();
    }

    private TypedActionResult<ItemStack> genericCast(Spell spell, World world, PlayerEntity user, Hand hand) {
        if (user.getStatusEffect(ElementalBattleStatusEffects.SPELL_LOCK_EFFECT) != null) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        if (spell != null) {
            if (world.isClient() && spell.clientCast(world, user)) {
                return TypedActionResult.success(user.getStackInHand(hand));
            }
            else if (spell.cast(world, user)) {
                return TypedActionResult.success(user.getStackInHand(hand));
            }
        }

        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    // Main Attack
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return genericCast(getUseSpell(user), world, user, hand);
    }

    public TypedActionResult<ItemStack> shield(World world, PlayerEntity user, Hand hand) {
        return genericCast(getShieldSpell(user), world, user, hand);
    }

    public TypedActionResult<ItemStack> dash(World world, PlayerEntity user, Hand hand) {
        return genericCast(getDashSpell(user), world, user, hand);
    }

    public TypedActionResult<ItemStack> areaAttack(World world, PlayerEntity user, Hand hand) {
        return genericCast(getAreaAttackSpell(user), world, user, hand);
    }

    public TypedActionResult<ItemStack> special(World world, PlayerEntity user, Hand hand) {
        return genericCast(getSpecialSpell(user), world, user, hand);
    }

    public TypedActionResult<ItemStack> ultimate(World world, PlayerEntity user, Hand hand) {
        return genericCast(getUltimateSpell(user), world, user, hand);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private MagicStaffItemRenderer renderer;

            @Override
            public MagicStaffItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new MagicStaffItemRenderer(id);

                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Idle", 0, this::idleAnimation));
    }

    private PlayState idleAnimation(AnimationState<GeoAnimatable> animatedItemAnimationState) {
        return animatedItemAnimationState.setAndContinue(RawAnimation.begin().thenLoop("animation." + id + ".idle"));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}