package io.github.mishkis.elemental_battle.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public abstract class MagicAreaAttackEntity extends MagicEntity {
    private PlayerEntity owner;
    private UUID ownerUuid;

    private ArrayList<LivingEntity> hit = new ArrayList<LivingEntity>();

    public MagicAreaAttackEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    protected abstract int getUptime();

    @Override
    public void tick() {
        super.tick();

        if (this.getOwner() == null || getUptime() < age) {
            this.discard();
        }
        else {
            this.setPosition(this.getOwner().getPos().offset(Direction.UP, 0.8));

            if (this.getWorld() instanceof ServerWorld serverWorld) {
                for (Entity entity : serverWorld.getOtherEntities(this.getOwner(), this.getBoundingBox(), entity -> {
                    return entity instanceof LivingEntity livingEntity && !hit.contains(livingEntity);
                })) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    hit.add(livingEntity);
                    onEntityCollision(livingEntity);
                }
            }

        }
    }

    protected abstract void onEntityCollision(LivingEntity entity);
}
