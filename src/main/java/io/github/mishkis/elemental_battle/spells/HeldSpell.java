package io.github.mishkis.elemental_battle.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public abstract class HeldSpell extends Spell {
    protected abstract int getMaxHeldTime();
    protected abstract int getCastDelay();
    protected abstract void onHeldCast(World world, PlayerEntity user);

    private boolean firstCast = false;
    private boolean firstClientCast = false;

    private int startCastTime = 0;
    private int lastCastAge = 0;

    protected int startClientCastTime = 0;
    private int lastClientCastAge = 0;

    public boolean isHeld() {
        return firstClientCast;
    }

    @Override
    protected boolean shouldAutoCooldown() {
        return false;
    }

    // Called when spell is held for maximum length of time.
    protected void onExpire(World world, PlayerEntity user) {}

    @Override
    protected void onRelease(World world, PlayerEntity user) {
        if (world.isClient) {
            firstClientCast = false;
            startClientCastTime = 0;
            lastClientCastAge = 0;
        } else {
            firstCast = false;
            startCastTime = 0;
            lastCastAge = 0;
        }

        user.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT).put(this, this.getCooldown());
    }

    @Override
    protected void onClientCast(World world, PlayerEntity user) {
        if (!firstClientCast) {
            firstClientCast = true;
            startClientCastTime = user.age;
        }

        if (user.age - lastClientCastAge > getCastDelay()) {
            onHeldCast(world, user);
            lastClientCastAge = user.age;
        }

        if (user.age >= startClientCastTime + getMaxHeldTime()) {
            onExpire(world, user);
            onRelease(world, user);
        }
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        if (!firstCast) {
            firstCast = true;
            startCastTime = user.age;
        }

        if (user.age - lastCastAge > getCastDelay()) {
            onHeldCast(world, user);
            lastCastAge = user.age;
        }

        if (user.age >= startCastTime + getMaxHeldTime()) {
            onExpire(world, user);
            onRelease(world, user);
        }
    }
}
