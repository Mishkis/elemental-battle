package io.github.mishkis.elemental_battle.entity.air_staff;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.MagicAreaAttackEntity;
import io.github.mishkis.elemental_battle.spells.air.SlamDownSpell;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AirLiftEntity extends MagicAreaAttackEntity implements GeoEntity {
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.air_lift.spawn");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AirLiftEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected int getUptime() {
        return 20;
    }

    @Override
    protected void onEntityCollision(LivingEntity entity) {
        entity.setVelocity(entity.getVelocity().x, Math.max(entity.getVelocity().y * 2, 1.5), entity.getVelocity().z);

        if (entity instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayer));
        }

        if (!this.getWorld().isClient) {
            SlamDownSpell.addToSlamDownList(entity, this.getOwner());
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", this::animation));
    }

    public <E extends AirLiftEntity> PlayState animation(AnimationState animationState) {
        return animationState.setAndContinue(ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
