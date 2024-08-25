package io.github.mishkis.elemental_battle.spells.air;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.air_staff.GustEntity;
import io.github.mishkis.elemental_battle.spells.EmpoweredSpell;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GustSpell extends Spell implements EmpoweredSpell {
    public static final AttachmentType<Boolean> EMPOWERED_ATTACHMENT = AttachmentRegistry.create(Identifier.of(ElementalBattle.MOD_ID, "gust_empowered_attachment"));

    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "gust");
    }

    @Override
    protected SpellElement getElement() {
        return SpellElement.AIR;
    }

    @Override
    protected Integer getCooldown() {
        return 50;
    }

    @Override
    public boolean isEmpowered(PlayerEntity user) {
        return user.getAttached(EMPOWERED_ATTACHMENT) != null && user.shouldIgnoreFallDamageFromCurrentExplosion() && !user.isOnGround();
    }

    @Override
    protected void onClientCast(World world, PlayerEntity user) {
        if (isEmpowered(user)) {
            user.removeAttached(EMPOWERED_ATTACHMENT);
        }
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        GustEntity gust = new GustEntity(ElementalBattleEntities.GUST, world);

        gust.setOwner(user);
        gust.setParentSpell(this);

        gust.setVelocity(user.getRotationVector());
        gust.setPosition(user.getEyePos().add(user.getRotationVector()));

        gust.setNoGravity(true);

        if (isEmpowered(user)) {
            user.removeAttached(EMPOWERED_ATTACHMENT);

            gust.setEmpowered();

            // Summons an additional 2 gust entities.
            for(int i = 0; i < 2; i++) {
                // All of this code is reused, might be worthwhile to simplify it so no repeating, but can't be bothered rn
                GustEntity surroundingGusts = new GustEntity(ElementalBattleEntities.GUST, world);

                surroundingGusts.setOwner(user);
                surroundingGusts.setParentSpell(this);

                // Taken from cone of fire, maybe make a helper class.
                Vec3d offset = new Vec3d(0.4, 0, 0).rotateZ((float) (Math.PI * i));

                offset = offset.rotateX((float) Math.toRadians(-user.getPitch()));
                offset = offset.rotateY((float) Math.toRadians(-user.getYaw()));

                Vec3d velocity = user.getRotationVector().add(offset);

                surroundingGusts.setVelocity(velocity);
                surroundingGusts.setPosition(user.getEyePos().add(velocity));

                surroundingGusts.setNoGravity(true);

                surroundingGusts.setEmpowered();

                world.spawnEntity(surroundingGusts);
            }
        }

        world.spawnEntity(gust);
    }
}
