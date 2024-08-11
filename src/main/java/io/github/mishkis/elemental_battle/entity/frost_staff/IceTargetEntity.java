package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.misc.ElementalBattleParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class IceTargetEntity extends Entity implements GeoEntity {
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.ice_target.spawn").thenPlay("animation.ice_target.despawn");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private Entity target;
    private UUID targetUuid;

    public IceTargetEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public void setTarget(Entity target) {
        if (target != null) {
            this.target = target;
            this.targetUuid = target.getUuid();
        }
    }

    public Entity getTarget() {
        if (this.target != null) {
            return this.target;
        }
        else {
            if (this.targetUuid != null && this.getWorld() instanceof ServerWorld world) {
                this.target = world.getPlayerByUuid(targetUuid);
                return this.target;
            }

            return null;
        }
    }

    @Override
    public void tick() {
        Entity target = this.getTarget();
        if (target != null) {
            this.setPosition(target.getPos());
            if (this.getWorld().isClient()) {
                this.getWorld().addParticle(ElementalBattleParticles.FROST_PARTICLE, this.getPos().getX() + random.nextBetween(-1, 1), this.getPos().getY() + 1, this.getPos().getZ() + random.nextBetween(-1, 1), 0, random.nextBetween(1, 3) * 0.1, 0);
            }
        }

        // 1.25 is the exact time it takes for ANIMATION to finish. Multiply by 20. Add a little so it can finish.
        if (26 < age) {
            this.discard();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", this::spawnAnimation));
    }

    private <E extends IceTargetEntity> PlayState spawnAnimation(final AnimationState<E> event) {
        return event.setAndContinue(ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.targetUuid != null) {
            nbt.putUuid("Target", this.targetUuid);
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Target")) {
            this.targetUuid = nbt.getUuid("Target");
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        return new EntitySpawnS2CPacket(this, entityTrackerEntry, target == null ? 0 : target.getId());
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);

        Entity target = this.getWorld().getEntityById(packet.getEntityData());
        if (target != null) {
            this.setTarget(target);
        }
    }
}
