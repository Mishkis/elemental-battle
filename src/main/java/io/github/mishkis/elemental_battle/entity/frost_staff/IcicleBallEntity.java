package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicProjectileEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.spells.SpellUltimateManager;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class IcicleBallEntity extends MagicProjectileEntity implements GeoEntity {
    private final RawAnimation SPAWN_ANIMATION = RawAnimation.begin().thenPlay("animation.icicle_ball.spawn").thenLoop("animation.icicle_ball.idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean isHit = false;

    public IcicleBallEntity(EntityType<? extends IcicleBallEntity> entityType, World world) {
        super(entityType, world);

        this.setNoGravity(true);
    }

    @Override
    protected void playTravelParticle(double x, double y, double z) {
        this.getWorld().addParticle(ElementalBattleParticles.FROST_PARTICLE, x, y + 0.5, z, 0.0, 0.0, 0.0);
    }

    @Override
    protected void playDiscardParticle(double x, double y, double z) {
        ((ServerWorld) this.getWorld()).spawnParticles(ElementalBattleParticles.FROST_SHATTER_PARTICLE, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 1);
    }

    @Override
    protected Box calculateBoundingBox() {
        EntityDimensions dimensions = this.getDimensions(this.getPose());

        if (!isHit) {
            dimensions = dimensions.scaled(3);

            return dimensions.getBoxAt(this.getPos().offset(Direction.DOWN, 0.45));
        }

        return dimensions.getBoxAt(this.getPos());
    }

    @Override
    public boolean canHit() {
        return !isHit;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.isHit && source.getSource() instanceof Entity entity) {
            this.isHit = true;

            if (entity instanceof Ownable ownable && ownable.getOwner() instanceof PlayerEntity player) {
                this.setOwner(player);
                entity = player;
            }
            else if(entity instanceof PlayerEntity player) {
                this.setOwner(player);
            }

            this.setVelocity(entity.getRotationVector().multiply(2));
        }

        return false;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        World world = this.getWorld();
        if (!world.isClient) {
            this.explode((ServerWorld) world);
        }
    }

    @Override
    protected void onEntityHit(Entity entity) {
       if (entity == this.getOwner()) {
           return;
       }

        entity.damage(this.getDamageSources().indirectMagic(this, this.getOwner()), this.getDamage() * 2);

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 2), this);
        }

        if (!this.getWorld().isClient && this.getOwner() != null) {
            this.getOwner().getAttachedOrCreate(SpellUltimateManager.SPELL_ULTIMATE_MANAGER_ATTACHMENT).add(this.getElement(), 10, this.getOwner());
        }
    }

    private void explode(ServerWorld world) {
        this.playDiscardParticle(getX(), getY(), getZ());

        int spawnCount = 8 + random.nextBetween(-1, 1);

        double startRotation = Math.toRadians(random.nextInt(45));

        for (int i = 0; i < spawnCount; i++) {
            Vec3d spawnPos = this.getPos().add(0, 0.2, 0);

            IcicleEntity icicle = new IcicleEntity(ElementalBattleEntities.ICICLE, world);
            icicle.setPosition(spawnPos);

            Vec3d velocity = new Vec3d(0.2, 0.3, 0);
            icicle.setVelocity(velocity.rotateY((float) (startRotation + (Math.PI * 2 * i)/spawnCount)));

            icicle.setDamage(this.getDamage());
            icicle.setUptime(200);

            icicle.setOwner(this.getOwner());
            icicle.setElement(this.getElement());

            world.spawnEntity(icicle);
        }

        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void onTimeOut() {
        if(this.getWorld() instanceof ServerWorld serverWorld) {
            this.explode(serverWorld);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", (animationState) -> animationState.setAndContinue(SPAWN_ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
