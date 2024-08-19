package io.github.mishkis.elemental_battle.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public abstract class Spell {
    protected enum Elements {
        FLAME,
        FROST,
        AIR,
    }

    protected abstract Elements getElement();

    protected abstract Integer getCooldown();

    protected abstract void onCast(World world, PlayerEntity user);

    public boolean cast(World world, PlayerEntity user) {
        SpellCooldownManager spellCooldownManager = user.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT);
        if (!spellCooldownManager.onCooldown(this)) {
            this.onCast(world, user);
            spellCooldownManager.put(this, getCooldown());
            return true;
        }
        return false;
    }
}