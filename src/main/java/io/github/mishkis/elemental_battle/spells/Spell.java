package io.github.mishkis.elemental_battle.spells;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.MagicEntity;
import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Arrays;

public abstract class Spell {
    private final Identifier ICON_PATH = Identifier.of(ElementalBattle.MOD_ID, "textures/spells/" + getElement().toString() + "/" + getId().getPath() + ".png");
    private final String SPELL_NAME = buildName();

    public abstract Identifier getId();

    public abstract SpellElement getElement();

    public abstract int getCooldown();

    public abstract String getDescription();

    protected abstract void onCast(World world, PlayerEntity user);

    public Identifier getIcon() {
        return ICON_PATH;
    }

    public String getSpellName() {
        return SPELL_NAME;
    }

    public float getDamage() {
        return 0;
    }

    public int getUptime() {
        return 0;
    }

    private String buildName() {
        StringBuilder stringBuilder = new StringBuilder();

        Arrays.stream(this.getId().getPath().split("_")).forEach((string) ->
                stringBuilder.append(StringUtil.capitalize(string)).append(" ")
        );

        return stringBuilder.toString();
    }

    protected MagicEntity genericEntity(PlayerEntity owner, MagicEntity entity) {
        entity.setDamage(getDamage());
        entity.setUptime(getUptime());
        entity.setOwner(owner);

        return entity;
    }

    // Override to add client cast effects.
    protected void onClientCast(World world, PlayerEntity user) {
    }

    // Override to allow casting only in certain conditions.
    public boolean canCast(World world, PlayerEntity user) {
        return true;
    }

    public boolean clientCast(World world, PlayerEntity user) {
        // This just syncs the spellComponent cooldown between server and client.
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