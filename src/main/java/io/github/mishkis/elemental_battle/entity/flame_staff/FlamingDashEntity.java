package io.github.mishkis.elemental_battle.entity.flame_staff;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.MagicDashEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.http.util.EntityUtils;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FlamingDashEntity extends MagicDashEntity implements GeoEntity {
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.flaming_dash.spawn").thenLoop("animation.flaming_dash.idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FlamingDashEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public float getBlocksTraveled() {
        return 10;
    }

    @Override
    public float getUptime() {
        return 20;
    }

    @Override
    public boolean isGrounded() {
        return false;
    }

    @Override
    public void playParticle(Vec3d pos) {
        this.getWorld().addParticle(ElementalBattleParticles.FLAME_PARTICLE_PARTIAL, pos.x + random.nextBetween(-1, 1), pos.y + 1, pos.z + random.nextBetween(-1, 1), 0, 0.2, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld() instanceof ServerWorld serverWorld) {
            for (Entity entity : serverWorld.getOtherEntities(this.getOwner(), this.getBoundingBox(), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR)) {
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.damage(this.getDamageSources().indirectMagic(this, this.getOwner()), 5);
                    livingEntity.setOnFireForTicks(60);
                }
            }
        }
    }

    public boolean canHit(Entity entity) {
        if (entity != this.getOwner()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", this::animation));
    }

    private <E extends FlamingDashEntity> PlayState animation(final AnimationState<E> animationState) {
        return animationState.setAndContinue(ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
