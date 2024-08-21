package io.github.mishkis.elemental_battle.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.item.helpers.MagicStaffItem;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellCooldownManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class SpellDisplay {
    public static void initialize() {
        HudRenderCallback.EVENT.register(((drawContext, tickCounter) -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;

            if (player.getMainHandStack().getItem() instanceof MagicStaffItem staff) {
                RenderSystem.enableBlend();
                drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/spell_display.png"), 0, drawContext.getScaledWindowHeight() - 42, 0, 0, 62, 42, 62, 42);
                RenderSystem.disableBlend();

                renderIcon("main", staff, drawContext);
                renderIcon("shield", staff, drawContext);
                renderIcon("dash", staff, drawContext);
                renderIcon("areaAttack", staff, drawContext);
                renderIcon("special", staff, drawContext);
                renderIcon("ultimate", staff, drawContext);
            }
        }));
    }

    private static void renderIcon(String slot, MagicStaffItem staff, DrawContext drawContext) {
        int x = 3;
        int y = 3;
        Spell spell = null;

        switch (slot) {
            case "main":
                spell = staff.getUseSpell();
                x = 3;
                y = drawContext.getScaledWindowHeight() - 39;
                break;
            case "shield":
                spell = staff.getShieldSpell();
                x = 23;
                y = drawContext.getScaledWindowHeight() - 39;
                break;
            case "dash":
                spell = staff.getDashSpell();
                x = 43;
                y = drawContext.getScaledWindowHeight() - 39;
                break;
            case "areaAttack":
                spell = staff.getAreaAttackSpell();
                x = 3;
                y = drawContext.getScaledWindowHeight() - 19;
                break;
            case "special":
                spell = staff.getSpecialSpell();
                x = 23;
                y = drawContext.getScaledWindowHeight() - 19;
                break;
            case "ultimate":
                spell = staff.getUltimateSpell();
                x = 43;
                y = drawContext.getScaledWindowHeight() - 19;
                break;
        }

        if (spell == null) {
            return;
        }

        if (spell.getIcon() != null) {
            // Main spell icon.
            drawContext.drawTexture(spell.getIcon(), x, y, 0, 0, 16, 16, 16, 16);

            // Renders overlay if the spell is disabled.
            RenderSystem.enableBlend();

            if (!spell.canCast(MinecraftClient.getInstance().world, MinecraftClient.getInstance().player)) {
                drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/disabled.png"), x, y, 0, 0, 16, 16, 16, 16);
            }

            // Renders cooldown overlay.
            SpellCooldownManager playerSpellCooldownManager = MinecraftClient.getInstance().player.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT);
            if (playerSpellCooldownManager.onCooldown(spell)) {
                drawContext.enableScissor(x, y + (int) (16 * (1 - playerSpellCooldownManager.percentageLeft(spell))), x + 16, y + 16);
                drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/on_cooldown.png"), x, y, 0, 0, 16, 16, 16, 16);
                drawContext.disableScissor();
            }

            RenderSystem.disableBlend();
        }
    }
}
