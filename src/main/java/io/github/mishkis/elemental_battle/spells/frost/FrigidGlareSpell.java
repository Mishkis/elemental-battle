package io.github.mishkis.elemental_battle.spells.frost;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.MagicDashEntity;
import io.github.mishkis.elemental_battle.entity.frost_staff.IceTargetEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
    public SpellElement getElement() {
        return SpellElement.FROST;
    }

    @Override
    public int getCooldown() {
        return 250;
    }

    @Override
    public String getDescription() {
        return "A powerful spell which transfers the cold of your eyes into the body of your enemy, freezing them.";
    }

    @Override
    public float getDamage() {
        return 3;
    }

    @Override
    public int getUptime() {
        return 25;
    }

    @Override
    public boolean canCast(World world, PlayerEntity user) {
        raycast(user);

        if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
            return (((EntityHitResult) hitResult).getEntity() instanceof LivingEntity livingEntity && livingEntity != user) || (((EntityHitResult) hitResult).getEntity() instanceof MagicDashEntity);
        }
        return false;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        Entity entity = ((EntityHitResult) hitResult).getEntity();
        LivingEntity target;

        if (entity instanceof LivingEntity livingEntity) {
            target = livingEntity;
        }
        else if (entity instanceof MagicDashEntity magicDash) {
            target = magicDash.getOwner();
        }
        else {
            return;
        }

        IceTargetEntity iceTarget = (IceTargetEntity) genericEntity(user, new IceTargetEntity(ElementalBattleEntities.ICE_TARGET, world));

        iceTarget.setTarget(target);
        iceTarget.setPosition(target.getPos());
        iceTarget.setFreezing();

        world.spawnEntity(iceTarget);
    }

    private void raycast(PlayerEntity user) {
        int distance = 1000;
        Vec3d start = user.getEyePos();
        Vec3d end = user.getEyePos().add(user.getRotationVector().multiply(distance));
        this.hitResult = ProjectileUtil.raycast(user, start, end, new Box(start.x, start.y, start.z, end.x, end.y, end.z), entity -> true, distance);
    }
}
