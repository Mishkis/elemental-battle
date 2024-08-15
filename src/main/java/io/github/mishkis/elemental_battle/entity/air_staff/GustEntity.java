package io.github.mishkis.elemental_battle.entity.air_staff;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GustEntity extends MagicProjectileEntity implements GeoEntity {
    private final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.gust.idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public float getDamage() {
        return 5;
    }

    public GustEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void playTravelParticle(double x, double y, double z) {

    }

    @Override
    protected void playDiscardParticle(double x, double y, double z) {

    }
    
    private void blowBackEntity(Entity entity) {
        Vec3d knockback_vec = entity.getPos().subtract(this.getPos());
        knockback_vec = knockback_vec.normalize().offset(Direction.UP, 0.5).multiply(2.5 - this.getPos().distanceTo(entity.getPos()) * 0.5);

        entity.setVelocity(knockback_vec);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity entity = entityHitResult.getEntity();
        entity.damage(this.getDamageSources().indirectMagic(this.getOwner(), entity), 5);

        onBlockHit();

        this.discard();
    }

    @Override
    protected void onBlockHit() {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            for (Entity entity : serverWorld.getOtherEntities(null, this.getBoundingBox().expand(5, 5, 5))) {
                blowBackEntity(entity);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", (animationState) -> {
            return animationState.setAndContinue(IDLE_ANIMATION); }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
