package io.github.mishkis.elemental_battle.spells;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;

public abstract class Spell {
    public abstract Identifier getId();

    protected abstract SpellElement getElement();

    protected abstract Integer getCooldown();

    protected abstract void onCast(World world, PlayerEntity user);

    public Identifier getIcon() {
        return Identifier.of(ElementalBattle.MOD_ID, "textures/spells/" + getElement().toString() + "/" + getId().getPath() + ".png");
    }

    // Override to allow casting only in certain conditions.
    public boolean canCast(World world, PlayerEntity user) {
        return true;
    }

    public boolean clientCast(World world, PlayerEntity user) {
        // This just syncs the spell cooldown between server and client. Make call onCast() if you need the client for something.
        SpellCooldownManager spellCooldownManager = user.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT);
        if (!spellCooldownManager.onCooldown(this) && this.canCast(world, user)) {
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