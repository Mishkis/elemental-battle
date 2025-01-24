package io.github.mishkis.elemental_battle.spells;

import io.github.mishkis.elemental_battle.ElementalBattle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public abstract class ToggledSpell extends Spell {
    @Override
    protected boolean shouldAutoCooldown() {
        return false;
    }

    protected abstract void onInitialCast(World world, PlayerEntity user);
    protected abstract void onToggle(World world, PlayerEntity user);

    private int startCastTime = Integer.MIN_VALUE;
    private int startClientCastTime = Integer.MIN_VALUE;
    private boolean releasedHold;
    private boolean releasedClientHold;

    // Can be called on server in Magic Staff Item.
    // Called on client every tick for spell renderer.
    // Kinda janky, but handles putting it on cooldown on expiration for the client and server will check on its own if it's on cooldown without cooldown manager.
    // Other solution would be to make this always tick on the server, but I'd rather not do that if it's not necessary.
    public boolean isToggled(World world, PlayerEntity user) {
        if (world.isClient) {
            if (startClientCastTime + getUptime() > user.age) {
                return true;
            } else if ((startClientCastTime + getUptime() + getCooldown() > user.age && !user.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT).onCooldown(this))) {
                startClientCastTime = Integer.MIN_VALUE;
                user.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT).put(this, getCooldown());
            }

            return false;
        }

        return startCastTime + getUptime() > user.age;
    }

    // This is used to make sure that holding the spell will not just auto turn it off.
    @Override
    protected void onRelease(World world, PlayerEntity user) {
        releasedHold = true;
        releasedClientHold = true;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        if (startCastTime + getUptime() < user.age) {
            // See comment above isToggled().
            if (startCastTime + getUptime() + getCooldown() < user.age) {
                startCastTime = user.age;
                releasedHold = false;
                onInitialCast(world, user);
            }
        } else if (releasedHold) {
            onToggle(world, user);
            startCastTime = Integer.MIN_VALUE;
            user.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT).put(this, getCooldown());
        }
    }

    @Override
    protected void onClientCast(World world, PlayerEntity user) {
        if (startClientCastTime + getUptime() < user.age) {
            startClientCastTime = user.age;
            releasedClientHold = false;
            onInitialCast(world, user);
        } else if (releasedClientHold) {
            onToggle(world, user);
            startClientCastTime = Integer.MIN_VALUE;
            user.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT).put(this, getCooldown());
        }
    }
}
