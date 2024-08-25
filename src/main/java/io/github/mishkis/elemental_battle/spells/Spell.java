package io.github.mishkis.elemental_battle.spells;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class Spell {
    private final Identifier ICON_PATH = Identifier.of(ElementalBattle.MOD_ID, "textures/spells/" + getElement().toString() + "/" + getId().getPath() + ".png");

    public abstract Identifier getId();

    protected abstract SpellElement getElement();

    protected abstract Integer getCooldown();

    protected abstract void onCast(World world, PlayerEntity user);

    public Identifier getIcon() {
        return ICON_PATH;
    }

    // Override to add client cast effects.
    protected void onClientCast(World world, PlayerEntity user) {
    }

    // Override to allow casting only in certain conditions.
    public boolean canCast(World world, PlayerEntity user) {
        return true;
    }

    public boolean clientCast(World world, PlayerEntity user) {
        // This just syncs the spell cooldown between server and client.
        SpellCooldownManager spellCooldownManager = user.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT);
        if (!spellCooldownManager.onCooldown(this) && this.canCast(world, user)) {
            this.onClientCast(world, user);
            spellCooldownManager.put(this, getCooldown());
            return true;
        }
        return false;
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