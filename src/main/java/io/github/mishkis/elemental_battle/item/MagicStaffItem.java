package io.github.mishkis.elemental_battle.item;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.item.armor.MagicArmorItem;
import io.github.mishkis.elemental_battle.rendering.TooltipSpellData;
import io.github.mishkis.elemental_battle.spells.*;
import io.github.mishkis.elemental_battle.spells.flame.ConeOfFireSpell;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
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

    public abstract SpellElement getElement();

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
        if (user.getAttachedOrCreate(SpellUltimateManager.SPELL_ULTIMATE_MANAGER_ATTACHMENT).getPercent(this.getElement()) >= 1) {
            for (ItemStack armor : user.getAllArmorItems()) {
                if (!(armor.getItem() instanceof MagicArmorItem magicArmorItem && magicArmorItem.getSpell().getElement() == this.getElement())) {
                    return null;
                }
            }
            return ultimateSpell();
        }
        return null;
    }

    private TypedActionResult<ItemStack> genericCast(Spell spell, World world, PlayerEntity user, Hand hand, Boolean released) {
        if (user.getStatusEffect(ElementalBattleStatusEffects.SPELL_LOCK_EFFECT) != null && !(spell instanceof ShieldSpell shieldSpell && shieldSpell.isToggled(world, user))) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        if (spell != null) {
            if (world.isClient() && spell.clientCast(world, user, released)) {
                return TypedActionResult.success(user.getStackInHand(hand));
            }
            else if (spell.cast(world, user, released)) {
                return TypedActionResult.success(user.getStackInHand(hand));
            }
        }

        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    public TypedActionResult<ItemStack> castByType(MagicStaffActions type, World world, PlayerEntity user, Hand hand, Boolean released) {
        return switch (type) {
            case SHIELD -> this.shield(world, user, hand, released);
            case DASH -> this.dash(world, user, hand, released);
            case AREA_ATTACK -> this.areaAttack(world, user, hand, released);
            case SPECIAL -> this.special(world, user, hand, released);
            case ULTIMATE -> this.ultimate(world, user, hand, released);
        };
    }

    // Main Attack
    // All of this code is here because I can't do the regular keybind system I have for getting the release of a keybind for RMB.
    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return Integer.MAX_VALUE;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (getUseSpell(user) instanceof HeldSpell) {
            if (!user.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT).onCooldown(getUseSpell(user))) {
                user.setCurrentHand(hand);
                return TypedActionResult.success(user.getStackInHand(hand));
            }

            return TypedActionResult.pass(user.getStackInHand(hand));
        }
        return genericCast(getUseSpell(user), world, user, hand, false);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            if (player.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT).onCooldown(getUseSpell(player))) {
                player.clearActiveItem();
            }
            genericCast(getUseSpell(player), world, player, player.getActiveHand(), false);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            genericCast(getUseSpell(player), player.getWorld(), player, player.getActiveHand(), true);
        }
    }

    private TypedActionResult<ItemStack> shield(World world, PlayerEntity user, Hand hand, Boolean released) {
        return genericCast(getShieldSpell(user), world, user, hand, released);
    }

    private TypedActionResult<ItemStack> dash(World world, PlayerEntity user, Hand hand, Boolean released) {
        return genericCast(getDashSpell(user), world, user, hand, released);
    }

    private TypedActionResult<ItemStack> areaAttack(World world, PlayerEntity user, Hand hand, Boolean released) {
        return genericCast(getAreaAttackSpell(user), world, user, hand, released);
    }

    private TypedActionResult<ItemStack> special(World world, PlayerEntity user, Hand hand, Boolean released) {
        return genericCast(getSpecialSpell(user), world, user, hand, released);
    }

    private TypedActionResult<ItemStack> ultimate(World world, PlayerEntity user, Hand hand, Boolean released) {
        TypedActionResult<ItemStack> result = genericCast(getUltimateSpell(user), world, user, hand, released);
        if (result.getResult().isAccepted()) {
            user.getAttachedOrCreate(SpellUltimateManager.SPELL_ULTIMATE_MANAGER_ATTACHMENT).reset(this.getElement());
        }

        return result;
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