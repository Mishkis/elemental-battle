package io.github.mishkis.elemental_battle.entity.air_staff;

import io.github.mishkis.elemental_battle.entity.MagicDashEntity;
import io.github.mishkis.elemental_battle.network.S2C.S2CSpellCooldownManagerRemove;
import io.github.mishkis.elemental_battle.particle.ElementalBattleParticles;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellCooldownManager;
import io.github.mishkis.elemental_battle.spells.air.DoubleDashSpell;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DoubleDashEntity extends MagicDashEntity implements GeoEntity {
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("animation.double_dash.spawn").thenLoop("animation.double_dash.idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private Spell parentSpell;

    public DoubleDashEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public float getBlocksTraveled() {
        return 8;
    }

    @Override
    public float getUptime() {
        return 10;
    }

    @Override
    public boolean isGrounded() {
        return false;
    }

    public void setParentSpell(Spell parentSpell) {
        this.parentSpell = parentSpell;
    }

    @Override
    protected void onDiscard() {
        if (this.getOwner() instanceof ServerPlayerEntity player) {
            if (player.getAttached(DoubleDashSpell.HAS_DOUBLE_DASHED_ATTACHMENT) == null) {
                player.setAttached(DoubleDashSpell.HAS_DOUBLE_DASHED_ATTACHMENT, true);

                player.getAttached(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT).remove(parentSpell);
                ServerPlayNetworking.send(player, new S2CSpellCooldownManagerRemove(parentSpell.getId()));
            }
            else {
                player.removeAttached(DoubleDashSpell.HAS_DOUBLE_DASHED_ATTACHMENT);
            }
        }
    }

    @Override
    public void playParticle(Vec3d pos) {
        this.getWorld().addParticle(ElementalBattleParticles.GUST_PARTICLE, pos.x + random.nextBetween(-10, 10) * 0.1, pos.y + random.nextBetween(10, 20) * 0.1, pos.z + random.nextBetween(-10, 10) * 0.1, 0, 0, 0);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", (animationState) ->
                animationState.setAndContinue(ANIMATION)
        ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
