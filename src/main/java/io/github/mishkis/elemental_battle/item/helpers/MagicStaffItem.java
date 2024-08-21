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
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

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

    private TypedActionResult<ItemStack> genericCast(Spell spell, World world, PlayerEntity user, Hand hand) {
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
        return genericCast(getUseSpell(), world, user, hand);
    }

    public TypedActionResult shield(World world, PlayerEntity user, Hand hand) {
        return genericCast(getShieldSpell(), world, user, hand);
    }

    public TypedActionResult dash(World world, PlayerEntity user, Hand hand) {
        return genericCast(getDashSpell(), world, user, hand);
    }

    public TypedActionResult areaAttack(World world, PlayerEntity user, Hand hand) {
        return genericCast(getAreaAttackSpell(), world, user, hand);
    }

    public TypedActionResult special(World world, PlayerEntity user, Hand hand) {
        return genericCast(getSpecialSpell(), world, user, hand);
    }

    public TypedActionResult ultimate(World world, PlayerEntity user, Hand hand) {
        return genericCast(getUltimateSpell(), world, user, hand);
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