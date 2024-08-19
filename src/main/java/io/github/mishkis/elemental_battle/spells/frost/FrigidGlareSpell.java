package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.IceTargetEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FrigidGlareSpell extends Spell {
    private HitResult hitResult;

    @Override
    protected Elements getElement() {
        return Elements.FROST;
    }

    @Override
    protected Integer getCooldown() {
        return 200;
    }

    @Override
    public boolean canCast(World world, PlayerEntity user) {
        raycast(user);

        if (this.hitResult.getType() == HitResult.Type.ENTITY) {
            return true;
        }
        return false;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        if (((EntityHitResult) hitResult).getEntity() instanceof LivingEntity livingEntity) {
            IceTargetEntity iceTarget = new IceTargetEntity(ElementalBattleEntities.ICE_TARGET, world);

            iceTarget.setTarget(livingEntity);
            iceTarget.setPosition(livingEntity.getPos());
            iceTarget.setFreezing();

            world.spawnEntity(iceTarget);
        }
    }

    private void raycast(PlayerEntity user) {
        Double distance = 1000d;
        Vec3d start = user.getEyePos();
        Vec3d end = user.getEyePos().add(user.getRotationVector().multiply(distance));
        this.hitResult = ProjectileUtil.raycast(user, start, end, new Box(start.x, start.y, start.z, end.x, end.y, end.z), entity -> {return true;}, distance);
    }
}
