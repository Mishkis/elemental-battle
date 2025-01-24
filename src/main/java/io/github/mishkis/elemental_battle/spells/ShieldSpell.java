package io.github.mishkis.elemental_battle.spells;

import io.github.mishkis.elemental_battle.entity.MagicEntity;
import io.github.mishkis.elemental_battle.entity.MagicShieldEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public abstract class ShieldSpell extends ToggledSpell {
    protected abstract MagicShieldEntity getShieldEntity(World world);

    private MagicEntity activeShield;

    @Override
    protected void onInitialCast(World world, PlayerEntity user) {
        activeShield = genericEntity(user, getShieldEntity(world));

        world.spawnEntity(activeShield);
    }

    @Override
    protected void onToggle(World world, PlayerEntity user) {
        activeShield.setUptime(0);
    }
}
