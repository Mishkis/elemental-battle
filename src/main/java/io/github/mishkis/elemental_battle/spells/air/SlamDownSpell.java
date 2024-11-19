package io.github.mishkis.elemental_battle.spells.air;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.network.S2C.S2CSlamDownAttachmentAdd;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import io.github.mishkis.elemental_battle.spells.SpellUltimateManager;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SlamDownSpell extends Spell {
    public static final AttachmentType<List<LivingEntity>> SLAM_DOWN_ATTACHMENT = AttachmentRegistry.createDefaulted(Identifier.of(ElementalBattle.MOD_ID, "slam_down_attachment"), ArrayList::new);

    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "slam_down");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.AIR;
    }

    @Override
    public float getDamage() {
        // It does about this much from testing.
        return 2.5f;
    }

    @Override
    public int getCooldown() {
        return 200;
    }

    @Override
    public String getDescription() {
        return "The clouds frown upon your foes, slamming them back onto the ground.";
    }

    public static void addToSlamDownList(Entity entity, PlayerEntity user) {
        if (entity instanceof LivingEntity livingEntity && !livingEntity.isRemoved() && livingEntity != user) {
            // Ensure only one copy of the entity exists in the list, and also refresh entity in case it is dead.
            user.getAttachedOrCreate(SlamDownSpell.SLAM_DOWN_ATTACHMENT).remove(livingEntity);
            user.getAttached(SlamDownSpell.SLAM_DOWN_ATTACHMENT).add(livingEntity);

            if (user instanceof ServerPlayerEntity serverPlayerEntity) {
                ServerPlayNetworking.send(serverPlayerEntity, new S2CSlamDownAttachmentAdd(livingEntity.getId()));
            }
        }
    }

    @Override
    public boolean canCast(World world, PlayerEntity user) {
        if (!user.getAttachedOrCreate(SLAM_DOWN_ATTACHMENT).isEmpty()) {
            for (Entity entity : user.getAttached(SLAM_DOWN_ATTACHMENT)) {
                if (!entity.isOnGround() && !entity.isRemoved()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void onClientCast(World world, PlayerEntity user) {
        user.removeAttached(SLAM_DOWN_ATTACHMENT);
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        user.getAttachedOrCreate(SLAM_DOWN_ATTACHMENT).forEach(entity -> {
            if (!entity.isOnGround() && !entity.isRemoved()) {
                entity.setVelocity(entity.getVelocity().x, Math.min(-5, entity.getVelocity().y * 2), entity.getVelocity().z);
                entity.fallDistance = 8;

                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ElementalBattleParticles.GUST_EXPLOSION_PARTICLE, entity.getX(), entity.getY() + 2, entity.getZ(), 3, 1, 0.2, 1, 1);
                    serverWorld.spawnParticles(ElementalBattleParticles.GUST_PARTICLE, entity.getX(), entity.getY() + 2, entity.getZ(), 5, 1, 1, 1, 2);
                }

                if (entity instanceof ServerPlayerEntity player) {
                    player.currentExplosionImpactPos = player.getPos();
                    player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
                }
            }
        });

        user.getAttachedOrCreate(SpellUltimateManager.SPELL_ULTIMATE_MANAGER_ATTACHMENT).add(this.getElement(), 5, user);
        user.removeAttached(SLAM_DOWN_ATTACHMENT);
    }
}
