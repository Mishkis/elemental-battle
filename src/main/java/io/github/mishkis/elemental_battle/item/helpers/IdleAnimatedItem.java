package io.github.mishkis.elemental_battle.item.helpers;

import io.github.mishkis.elemental_battle.item.helpers.client.AnimatedItemRenderer;
import net.minecraft.item.Item;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.function.Consumer;

public class IdleAnimatedItem extends Item implements GeoItem {
    private final String id;

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public IdleAnimatedItem(String id, Settings settings) {
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