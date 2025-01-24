package io.github.mishkis.elemental_battle.entity.air_staff;

import io.github.mishkis.elemental_battle.entity.MagicShieldEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.spells.SpellUltimateManager;
import io.github.mishkis.elemental_battle.spells.air.SlamDownSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BlowBackEntity extends MagicShieldEntity implements GeoEntity {
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.double_dash.spawn").thenLoop("animation.double_dash.idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BlowBackEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void playParticle(Vec3d pos) {
        this.getWorld().addParticle(ElementalBattleParticles.GUST_PARTICLE, pos.x + random.nextBetween(-10, 10) * 0.1, pos.y + random.nextBetween(10, 20) * 0.1, pos.z + random.nextBetween(-10, 10) * 0.1, 0, 0, 0);
    }

    @Override
    public void onDamaged(DamageSource damageSource) {
        Entity entity = damageSource.getAttacker();

        if (entity == null) {
            return;
        }

        Vec3d knockback_vec = entity.getEyePos().subtract(this.getPos()).multiply(1, 0, 1);
        knockback_vec = knockback_vec.normalize().multiply(2).add(0, 1, 0);

        entity.setVelocity(knockback_vec);

        if (!this.getWorld().isClient && this.getOwner() != null) {
            ((ServerWorld) this.getWorld()).spawnParticles(ElementalBattleParticles.GUST_EXPLOSION_PARTICLE, entity.getX(), entity.getY() + 0.5, entity.getZ(), 2, 0.5, 0.5, 0.5, 1);
            this.getOwner().getAttachedOrCreate(SpellUltimateManager.SPELL_ULTIMATE_MANAGER_ATTACHMENT).add(this.getElement(), 5, this.getOwner());
            SlamDownSpell.addToSlamDownList(entity, this.getOwner());
        }

        if (entity instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayer));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", (animationState) ->
            animationState.setAndContinue(ANIMATION)
        ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
