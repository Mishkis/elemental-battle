package io.github.mishkis.elemental_battle.entity.flame_staff;

import io.github.mishkis.elemental_battle.entity.MagicAreaAttackEntity;
import io.github.mishkis.elemental_battle.spells.SpellUltimateManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FlameVortexEntity extends MagicAreaAttackEntity implements GeoEntity {
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.flame_vortex.spawn");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FlameVortexEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void onEntityCollision(LivingEntity entity) {
        entity.damage(this.getDamageSources().indirectMagic(this, this.getOwner()), this.getDamage());
        entity.setOnFireForTicks(60);

        if (this.getOwner() != null) {
            entity.setVelocity(this.getOwner().getPos().subtract(entity.getPos()).normalize());

            this.getOwner().getAttachedOrCreate(SpellUltimateManager.SPELL_ULTIMATE_MANAGER_ATTACHMENT).add(this.getElement(), 5, this.getOwner());
        }
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "spawn", (animationState) -> animationState.setAndContinue(ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
