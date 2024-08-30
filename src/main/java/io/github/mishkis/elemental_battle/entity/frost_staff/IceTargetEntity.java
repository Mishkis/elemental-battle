package io.github.mishkis.elemental_battle.entity.frost_staff;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicEntity;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.rendering.SpellDisplay;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class IceTargetEntity extends MagicEntity implements GeoEntity {
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.ice_target.spawn").thenPlay("animation.ice_target.despawn");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // This is honestly not a very good way to sync target between server and client since it sacrifices remembering original owner.
    private Entity target;
    private UUID targetUuid;

    // Controls whether this should summon a Frozen Solid entity or not.
    private Boolean freezing = false;

    public IceTargetEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    // This has a constant uptime.
    // 1.25 is the exact time it takes for ANIMATION to finish. Multiply by 20, add a little so it can finish.
    @Override
    public Integer getUptime() {
        return 26;
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

    public void setFreezing() {
        this.freezing = true;
    }

    @Override
    public void tick() {
        super.tick();

        Entity target = this.getTarget();
        if (target != null && !target.isRemoved()) {
            this.setPosition(target.getPos());
            if (this.getWorld().isClient()) {
                this.getWorld().addParticle(ElementalBattleParticles.FROST_PARTICLE, this.getX() + random.nextBetween(-1, 1), this.getY() + 1, this.getZ() + random.nextBetween(-1, 1), 0, random.nextBetween(1, 3) * 0.1, 0);
            }

            // Do this before time out to ensure that it is in fact called.
            if (getUptime() - 1 < age && this.getWorld().isClient) {
                target.removeAttached(SpellDisplay.SPELL_DISPLAY_SHIELD_WARNING_ATTACHMENT);
            }
        }
        else {
            this.discard();
        }
    }

    @Override
    protected void onTimeOut() {
        if (!this.getWorld().isClient) {
            if (freezing && this.getTarget() instanceof LivingEntity livingTarget && livingTarget.getStatusEffect(ElementalBattleStatusEffects.SHIELD_EFFECT) == null) {
                FrozenSolidEntity frozenSolid = new FrozenSolidEntity(ElementalBattleEntities.FROZEN_SOLID, this.getWorld());

                frozenSolid.setTarget(livingTarget);
                frozenSolid.setPosition(livingTarget.getPos());

                this.getWorld().spawnEntity(frozenSolid);
            }
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
            target.setAttached(SpellDisplay.SPELL_DISPLAY_SHIELD_WARNING_ATTACHMENT, true);
        }
    }
}
