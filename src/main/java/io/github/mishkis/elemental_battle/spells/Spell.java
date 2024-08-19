package io.github.mishkis.elemental_battle.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class Spell {
    protected enum Elements {
        FLAME,
        FROST,
        AIR,
    }

    protected abstract Elements getElement();

    protected abstract Integer getCooldown();

    public abstract Identifier getIcon();

    protected abstract void onCast(World world, PlayerEntity user);

    // Override to allow casting only in certain conditions.
    public boolean canCast(World world, PlayerEntity user) {
        return true;
    }

    public boolean cast(World world, PlayerEntity user) {
        SpellCooldownManager spellCooldownManager = user.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT);
        if (!spellCooldownManager.onCooldown(this) && this.canCast(world, user)) {
            this.onCast(world, user);
            spellCooldownManager.put(this, getCooldown());
            return true;
        }
        return false;
    }
}