package io.github.mishkis.elemental_battle.item.helpers;

import io.github.mishkis.elemental_battle.item.helpers.client.MagicStaffItemRenderer;
import io.github.mishkis.elemental_battle.spells.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
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

import java.util.function.Consumer;

public abstract class MagicStaffItem extends Item implements GeoItem {
    private final String id;

    @Nullable
    public abstract Spell getUseSpell();

    @Nullable
    public abstract Spell getShieldSpell();

    @Nullable
    public abstract Spell getDashSpell();

    @Nullable
    public abstract Spell getAreaAttackSpell();

    @Nullable
    public abstract Spell getSpecialSpell();

    @Nullable
    public abstract Spell getUltimateSpell();

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    // Main Attack
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        if (getUseSpell() == null || !getUseSpell().cast(world, user)) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public TypedActionResult shield(World world, PlayerEntity user, Hand hand) {
        if (getShieldSpell() == null || !getShieldSpell().cast(world, user)) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public TypedActionResult dash(World world, PlayerEntity user, Hand hand) {
        if (getDashSpell() == null || !getDashSpell().cast(world, user)) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public TypedActionResult areaAttack(World world, PlayerEntity user, Hand hand) {
        if (getAreaAttackSpell() == null || !getAreaAttackSpell().cast(world, user)) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public TypedActionResult special(World world, PlayerEntity user, Hand hand) {
        if (getSpecialSpell() == null || !getSpecialSpell().cast(world, user)) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public TypedActionResult ultimate(World world, PlayerEntity user, Hand hand) {
        if (getUltimateSpell() == null || !getUltimateSpell().cast(world, user)) {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public MagicStaffItem(String id, Settings settings) {
        super(settings);

        this.id = id;

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
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