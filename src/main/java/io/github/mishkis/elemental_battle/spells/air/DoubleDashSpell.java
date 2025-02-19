package io.github.mishkis.elemental_battle.spells.air;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.ElementalBattleEntities;
import io.github.mishkis.elemental_battle.entity.air_staff.DoubleDashEntity;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellElement;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DoubleDashSpell extends Spell {
    public static final AttachmentType<Boolean> HAS_DOUBLE_DASHED_ATTACHMENT = AttachmentRegistry.create(Identifier.of(ElementalBattle.MOD_ID, "has_double_dashed_attachment"));

    @Override
    public Identifier getId() {
        return Identifier.of(ElementalBattle.MOD_ID, "double_dash");
    }

    @Override
    public SpellElement getElement() {
        return SpellElement.AIR;
    }

    @Override
    public int getCooldown() {
        return 300;
    }

    @Override
    public String getDescription() {
        return "The clouds favour you, allowing you to propel yourself forward twice.";
    }

    @Override
    public int getUptime() {
        return 10;
    }

    @Override
    protected void onCast(World world, PlayerEntity user) {
        DoubleDashEntity doubleDash = (DoubleDashEntity) genericEntity(user, new DoubleDashEntity(ElementalBattleEntities.DOUBLE_DASH, world));

        doubleDash.setParentSpell(this);

        world.spawnEntity(doubleDash);
    }
}
