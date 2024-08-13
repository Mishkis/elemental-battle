package io.github.mishkis.elemental_battle.entity.flame_staff;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.MagicShieldEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WallOfFireEntity extends MagicShieldEntity implements GeoEntity {
    // We use the same model for both Wall of Fire and Flaming Dash.
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.flaming_dash.spawn").thenLoop("animation.flaming_dash.idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    public WallOfFireEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void playParticle(Vec3d pos) {
        this.getWorld().addParticle(ElementalBattleParticles.FLAME_PARTICLE_SMOKE, pos.x + random.nextBetween(-1, 1), pos.y + 1, pos.z + random.nextBetween(-1, 1), 0, 0.2, 0);
    }

    @Override
    public void shieldEffect(PlayerEntity owner) {
        super.shieldEffect(owner);

        owner.setOnFireForTicks(10);
        owner.setStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10, 1), this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", this::animation));
    }

    public <E extends WallOfFireEntity> PlayState animation(AnimationState<E> animationState) {
        return animationState.setAndContinue(ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
