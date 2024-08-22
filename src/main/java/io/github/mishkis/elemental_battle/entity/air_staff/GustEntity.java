package io.github.mishkis.elemental_battle.entity.air_staff;

import io.github.mishkis.elemental_battle.entity.MagicProjectileEntity;
import io.github.mishkis.elemental_battle.network.S2CSpellCooldownManagerRemove;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellCooldownManager;
import io.github.mishkis.elemental_battle.spells.air.GustSpell;
import io.github.mishkis.elemental_battle.spells.air.SlamDownSpell;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GustEntity extends MagicProjectileEntity implements GeoEntity {
    private final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.gust.idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean empowered = false;
    private Spell parentSpell;

    @Override
    public float getDamage() {
        return 5;
    }

    public void setEmpowered() {
        this.setVelocity(this.getVelocity().multiply(2));

        empowered = true;
    }

    public void setParentSpell(Spell parentSpell) {
        this.parentSpell = parentSpell;
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

        if (entity != this.getOwner()) {
            if (empowered) {
                entity.damage(this.getDamageSources().indirectMagic(this.getOwner(), entity), 5);

                if (knockback_vec.y < 1) {
                    knockback_vec = new Vec3d(knockback_vec.x, 1, knockback_vec.z);
                }

                knockback_vec = knockback_vec.multiply(1.2);
            }

            if (!this.getWorld().isClient && this.getOwner() instanceof PlayerEntity user) {
                SlamDownSpell.addToSlamDownList(entity, user);
            }
        }

        entity.setVelocity(knockback_vec);

        if (entity instanceof ServerPlayerEntity player) {
            player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));

            if (player == this.getOwner()) {
                player.currentExplosionImpactPos = player.getPos();
                player.setIgnoreFallDamageFromCurrentExplosion(true);

                player.setAttached(GustSpell.EMPOWERED_ATTACHMENT, true);

                if (!empowered && parentSpell != null) {
                    player.getAttached(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT).remove(parentSpell);
                    ServerPlayNetworking.send(player, new S2CSpellCooldownManagerRemove(parentSpell.getId()));
                }
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
