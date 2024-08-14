package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class FrozenSolidEntity extends Entity implements GeoEntity {
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.frozen_solid.spawn");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private LivingEntity target;
    private UUID targetUuid;

    private int uptime = 100;
    private float ownerStartHealth = 0;

    public FrozenSolidEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public void setTarget (LivingEntity target) {
        if (target != null) {
            this.target = target;
            this.targetUuid = target.getUuid();
        }
    }

    @Nullable
    public LivingEntity getTarget() {
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

    private void playShatterParticle(ServerWorld world, double x, double y, double z) {
        world.spawnParticles(ElementalBattleParticles.FROST_SHATTER_PARTICLE, x, y + 1, z, 3, 1, 0.2, 1, 1);
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity target = this.getTarget();

        if (this.getWorld() instanceof ServerWorld serverWorld && target == null) {
            playShatterParticle(serverWorld, this.getX(), this.getY(), this.getZ());
            this.discard();
        }
        else if (target == null) {
            this.discard();
        }
        else {
            if (ownerStartHealth == 0) {
                ownerStartHealth = target.getHealth();
            }
            target.setVelocity(Vec3d.ZERO);

            this.setPosition(target.getPos());

            if (target instanceof HostileEntity hostileTarget) {
                hostileTarget.setAiDisabled(true);
            }

            if (this.getWorld() instanceof ServerWorld serverWorld && (uptime < age || target.getHealth() < ownerStartHealth)) {
                if (target instanceof HostileEntity hostileTarget) {
                    hostileTarget.setAiDisabled(false);
                }

                target.damage(this.getDamageSources().freeze(), 3);

                playShatterParticle(serverWorld, this.getX(), this.getY(), this.getZ());
                this.discard();
            }
        }
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

        LivingEntity target = (LivingEntity) this.getWorld().getEntityById(packet.getEntityData());
        if (target != null) {
            this.setTarget(target);
        }
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", this::animation));
    }

    private <E extends FrozenSolidEntity> PlayState animation(AnimationState animationState) {
        return animationState.setAndContinue(ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
