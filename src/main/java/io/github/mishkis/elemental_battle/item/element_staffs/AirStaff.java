package io.github.mishkis.elemental_battle.item.element_staffs;

import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import io.github.mishkis.elemental_battle.spells.air.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class AirStaff extends MagicStaffItem {
    public AirStaff() {
        super("air_staff");
    }

    private final Spell useSpell = new GustSpell();

    @Override
    public SpellElement getElement() {
        return SpellElement.AIR;
    }

    @Override
    protected void hitAttack(LivingEntity target, PlayerEntity player) {
        if (player.getWorld() instanceof ServerWorld world) {
            world.spawnParticles(ElementalBattleParticles.GUST_EXPLOSION_PARTICLE, target.getX(), target.getY() + 0.5, target.getZ(), 3, 0.4, 0.1, 0.4, 1);

            SlamDownSpell.addToSlamDownList(target, player);
        }

        Vec3d knockback_vec = target.getPos().subtract(player.getPos());
        knockback_vec = knockback_vec.multiply(1, 0, 1);
        knockback_vec = knockback_vec.normalize().multiply(1.5);
        knockback_vec = knockback_vec.add(0, 0.75, 0);
        target.setVelocity(knockback_vec);
    }

    @Override
    public @Nullable Spell useSpell() {
        return useSpell;
    }

    @Override
    public @Nullable Spell ultimateSpell() {
        return new GustSpell();
    }
}
