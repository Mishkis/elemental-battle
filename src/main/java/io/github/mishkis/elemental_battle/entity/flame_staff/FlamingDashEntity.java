package io.github.mishkis.elemental_battle.entity.flame_staff;

import io.github.mishkis.elemental_battle.entity.MagicDashEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.spells.SpellUltimateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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
            for (Entity entity : serverWorld.getOtherEntities(this.getOwner(), this.getBoundingBox(), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.and(entity -> entity != this))) {
                entity.damage(this.getDamageSources().indirectMagic(this, this.getOwner()), this.getDamage());
                entity.setOnFireForTicks(60);

                this.getOwner().getAttachedOrCreate(SpellUltimateManager.SPELL_ULTIMATE_MANAGER_ATTACHMENT).add(this.getElement(), 5, this.getOwner());
            }
        }
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", (animationState) -> animationState.setAndContinue(ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
