package io.github.mishkis.elemental_battle.entity.air_staff;

import io.github.mishkis.elemental_battle.entity.MagicAreaAttackEntity;
import io.github.mishkis.elemental_battle.spells.SpellUltimateManager;
import io.github.mishkis.elemental_battle.spells.air.SlamDownSpell;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
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
    protected double getOffset() {
        return 0;
    }

    @Override
    protected void onEntityCollision(LivingEntity entity) {
        entity.setVelocity(entity.getVelocity().x, Math.max(entity.getVelocity().y * 2, 1.5), entity.getVelocity().z);

        entity.setVelocity(entity.getVelocity().add(entity.getPos().subtract(this.getPos()).multiply(1, 0, 1).normalize().multiply(2)));

        if (entity instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayer));
        }

        if (!this.getWorld().isClient) {
            this.getOwner().getAttachedOrCreate(SpellUltimateManager.SPELL_ULTIMATE_MANAGER_ATTACHMENT).add(this.getElement(), 2, this.getOwner());
            SlamDownSpell.addToSlamDownList(entity, this.getOwner());
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", (animationState) -> animationState.setAndContinue(ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
