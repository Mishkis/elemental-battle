package io.github.mishkis.elemental_battle.item.helpers;

import io.github.mishkis.elemental_battle.item.helpers.client.AnimatedItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
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

    // Main Attack
    @Override
    public abstract TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand);

    public abstract TypedActionResult shield(World world, PlayerEntity user, Hand hand);

    public abstract TypedActionResult dash(World world, PlayerEntity user, Hand hand);

    public abstract TypedActionResult areaAttack(World world, PlayerEntity user, Hand hand);

    public abstract TypedActionResult special(World world, PlayerEntity user, Hand hand);

    public abstract TypedActionResult ultimate(World world, PlayerEntity user, Hand hand);

    public MagicStaffItem(String id, Settings settings) {
        super(settings);

        this.id = id;

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private AnimatedItemRenderer renderer;

            @Override
            public AnimatedItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new AnimatedItemRenderer(id);

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