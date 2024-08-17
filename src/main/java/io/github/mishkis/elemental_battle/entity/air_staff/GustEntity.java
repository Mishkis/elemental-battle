package io.github.mishkis.elemental_battle.entity.air_staff;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.MagicProjectileEntity;
import io.github.mishkis.elemental_battle.item.AirStaff;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GustEntity extends MagicProjectileEntity implements GeoEntity {
    private final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.gust.idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final AttachmentType<Boolean> EMPOWERED_ATTACHMENT = AttachmentRegistry.create(Identifier.of(ElementalBattle.MOD_ID, "gust_empowered_attachment"));

    private boolean empowered = false;

    @Override
    public float getDamage() {
        return 5;
    }

    public void setEmpowered() {
        this.setVelocity(this.getVelocity().multiply(2));

        empowered = true;
    }

    public GustEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void playTravelParticle(double x, double y, double z) {
        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ElementalBattleParticles.GUST_PARTICLE, x + random.nextBetween(-5, 5) * 0.1, y + random.nextBetween(-5, 5) * 0.1, z + random.nextBetween(-5, 5) * 0.1, 0, 0, 0);
        }
    }

    @Override
    protected void playDiscardParticle(double x, double y, double z) {
        ((ServerWorld) this.getWorld()).spawnParticles(ElementalBattleParticles.GUST_EXPLOSION_PARTICLE, x, y + 0.5, z, 3, 1, 0.2, 1, 1);
        ((ServerWorld) this.getWorld()).spawnParticles(ElementalBattleParticles.GUST_PARTICLE, x, y + 0.5, z, 5, 1, 1, 1, 2);
    }
    
    private void blowBackEntity(Entity entity) {
        Vec3d knockback_vec = entity.getEyePos().subtract(this.getPos());
        knockback_vec = knockback_vec.normalize().multiply(2 / knockback_vec.length());

        if (empowered && entity != this.getOwner()) {
            entity.damage(this.getDamageSources().indirectMagic(this.getOwner(), entity), 5);

            if (knockback_vec.y < 1) {
                knockback_vec = new Vec3d(knockback_vec.x, 1, knockback_vec.z);
            }

            knockback_vec = knockback_vec.multiply(1.2);
        }

        entity.setVelocity(knockback_vec);

        if (entity instanceof ServerPlayerEntity player) {
            player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));

            if (player == this.getOwner()) {
                player.currentExplosionImpactPos = player.getPos();
                player.setIgnoreFallDamageFromCurrentExplosion(true);

                player.setAttached(EMPOWERED_ATTACHMENT, true);
            }
        }
        else if (entity instanceof ProjectileEntity projectileEntity) {
            projectileEntity.setOwner(this.getOwner());
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity entity = entityHitResult.getEntity();
        if (entity != this.getOwner()) {
            entity.damage(this.getDamageSources().indirectMagic(this.getOwner(), entity), 5);
        }

        onBlockHit();

        if (!this.getWorld().isClient()) {
            playDiscardParticle(this.getX(), this.getY(), this.getZ());
        }

        this.discard();
    }

    @Override
    protected void onBlockHit() {
        for (Entity entity : this.getWorld().getOtherEntities(null, this.getBoundingBox().expand(3, 3, 3))) {
            blowBackEntity(entity);
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
