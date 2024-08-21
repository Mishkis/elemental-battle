package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.frost_staff.IceTargetEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FrigidGlareSpell extends Spell {
    private HitResult hitResult;

    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "frigid_glare");
    }

    @Override
    protected SpellElement getElement() {
        return SpellElement.FROST;
    }

    @Override
    protected Integer getCooldown() {
        return 200;
    }

    @Override
    public boolean canCast(World world, PlayerEntity user) {
        raycast(user);

        if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
            if (((EntityHitResult) hitResult).getEntity() instanceof LivingEntity livingEntity && livingEntity != user) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        LivingEntity livingEntity = (LivingEntity) ((EntityHitResult) hitResult).getEntity();

        IceTargetEntity iceTarget = new IceTargetEntity(ElementalBattleEntities.ICE_TARGET, world);

        iceTarget.setTarget(livingEntity);
        iceTarget.setPosition(livingEntity.getPos());
        iceTarget.setFreezing();

        world.spawnEntity(iceTarget);
    }

    private void raycast(PlayerEntity user) {
        Double distance = 1000d;
        Vec3d start = user.getEyePos();
        Vec3d end = user.getEyePos().add(user.getRotationVector().multiply(distance));
        this.hitResult = ProjectileUtil.raycast(user, start, end, new Box(start.x, start.y, start.z, end.x, end.y, end.z), entity -> {return true;}, distance);
    }
}
