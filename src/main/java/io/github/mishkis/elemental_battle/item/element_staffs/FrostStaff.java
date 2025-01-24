package io.github.mishkis.elemental_battle.item.element_staffs;

import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import io.github.mishkis.elemental_battle.spells.frost.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

public class FrostStaff extends MagicStaffItem {
    public FrostStaff() {
        super("frost_staff");
    }

    private final Spell useSpell = new IcicleBallSpell();

    @Override
    public SpellElement getElement() {
        return SpellElement.FROST;
    }

    @Override
    protected void hitAttack(LivingEntity target, PlayerEntity player) {
        if (player.getWorld() instanceof ServerWorld world) {
            world.spawnParticles(ElementalBattleParticles.FROST_SHATTER_PARTICLE, target.getX(), target.getEyeY(), target.getZ(), 3, 0.4, 0.4, 0.4, 1);
        }

        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 0));
    }

    @Override
    public @Nullable Spell useSpell() {
        return useSpell;
    }

    @Override
    public @Nullable Spell ultimateSpell() {
        return new IcicleBallSpell();
    }
}
