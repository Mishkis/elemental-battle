package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicShieldEntity;
import io.github.mishkis.elemental_battle.misc.ElementalBattleParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ShatteringWallEntity extends MagicShieldEntity implements GeoEntity {
    private final RawAnimation SPAWN_ANIMATION = RawAnimation.begin().thenPlay("animation.shattering_wall.spawn").thenLoop("animation.shattering_wall.idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ShatteringWallEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void playParticle(Vec3d pos) {
        this.getWorld().addParticle(ElementalBattleParticles.FROST_PARTICLE, pos.getX() + random.nextBetween(-1, 1), pos.getY() + 1, pos.getZ() + random.nextBetween(-1, 1), 0, random.nextBetween(1, 3) * 0.1, 0);
    }

    @Override
    public void shieldEffect(PlayerEntity owner) {
        super.shieldEffect(owner);
        owner.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 2));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.getWorld().isClient()) {
            IcicleEntity icicle = new IcicleEntity(ElementalBattleEntities.ICICLE, this.getWorld());

            // Make icicle target hit source.
            Entity entity = source.getSource();
            if (entity instanceof Ownable) {
                Entity target = ((Ownable) entity).getOwner();

                if (target != null) {
                    if (target == this.getOwner()) {
                        return true;
                    }

                    icicle.setTarget(target);

                    // Spawn a target on the entity
                    IceTargetEntity iceTarget = new IceTargetEntity(ElementalBattleEntities.ICE_TARGET, this.getWorld());

                    iceTarget.setTarget(target);
                    iceTarget.setPosition(target.getPos());

                    this.getWorld().spawnEntity(iceTarget);
                }
            }

            icicle.setOwner(this.getOwner());
            icicle.setDamage(5);
            icicle.setPosition(this.getPos().offset(Direction.UP, 1));

            icicle.setVelocity(random.nextBetween(-10, 10) * 0.1, 0.3, random.nextBetween(-10, 10) * 0.1);

            this.getWorld().spawnEntity(icicle);
        }

        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", this::spawnAnimation));
    }

    private <E extends ShatteringWallEntity> PlayState spawnAnimation(final AnimationState<E> event) {
        return event.setAndContinue(SPAWN_ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
