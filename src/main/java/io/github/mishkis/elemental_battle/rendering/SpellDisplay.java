package io.github.mishkis.elemental_battle.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.network.ElementalBattleNetworkClient;
import io.github.mishkis.elemental_battle.spells.EmpoweredSpell;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellCooldownManager;
import io.github.mishkis.elemental_battle.spells.SpellUltimateManager;
import io.github.mishkis.elemental_battle.status_effects.ElementalBattleStatusEffects;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;

public class SpellDisplay {
    public static final AttachmentType<Boolean> SPELL_DISPLAY_SHIELD_WARNING_ATTACHMENT = AttachmentRegistry.create(Identifier.of(ElementalBattle.MOD_ID, "spell_display_shield_warning_attachment"));

    private static final Vector2i position = new Vector2i(0, 0);

    private static double last_ultimate_percent = 0;
    private static boolean gaining_ultimate = true;
    private static double ultimate_white_bar_percentage = 0;
    private static double ultimate_colored_bar_percentage = 0;

    public static void initialize() {
        HudRenderCallback.EVENT.register(((drawContext, tickCounter) -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;

            if (player.getMainHandStack().getItem() instanceof MagicStaffItem staff) {
                position.x = drawContext.getScaledWindowWidth() - 387;
                position.y = drawContext.getScaledWindowHeight() - 90;

                RenderSystem.enableBlend();
                drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/spell_display.png"), position.x, position.y, 0, 0, 120, 88, 120, 88);
                RenderSystem.disableBlend();

                renderIcon("main", staff, drawContext);
                renderIcon("shield", staff, drawContext);
                renderIcon("dash", staff, drawContext);
                renderIcon("areaAttack", staff, drawContext);
                renderIcon("special", staff, drawContext);
                renderIcon("ultimate", staff, drawContext);

                if (player.getAttached(SPELL_DISPLAY_SHIELD_WARNING_ATTACHMENT) != null && !player.hasStatusEffect(ElementalBattleStatusEffects.SHIELD_EFFECT)) {
                    drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/shield_alert.png"), drawContext.getScaledWindowWidth()/2 - 17, drawContext.getScaledWindowHeight()/2 - 11, 1000, 0, 0, 34, 18, 34, 18);
                }

                // Draw ultimate bar
                double percent = player.getAttachedOrCreate(SpellUltimateManager.SPELL_ULTIMATE_MANAGER_ATTACHMENT).getPercent(staff.getElement());

                if (percent > last_ultimate_percent) {
                    gaining_ultimate = true;
                }

                if (percent < last_ultimate_percent) {
                    gaining_ultimate = false;
                }

                ultimate_white_bar_percentage = MathHelper.lerp(gaining_ultimate ? 0.5 : 0.1, ultimate_white_bar_percentage, percent);
                ultimate_colored_bar_percentage = MathHelper.lerp(gaining_ultimate ? 0.1 : 0.5, ultimate_colored_bar_percentage, percent);

                RenderSystem.enableBlend();

                drawContext.enableScissor(position.x + 40, position.y + 60 - (int)(32 * ultimate_white_bar_percentage), position.x + 80, position.y + 60);
                drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/ultimate_hexagon.png"), position.x + 40, position.y + 28, 0, 0, 40, 32, 40, 32);
                drawContext.disableScissor();

                int color = staff.getElement().getColor();
                drawContext.setShaderColor(ColorHelper.Argb.getRed(color)/255f, ColorHelper.Argb.getGreen(color)/255f, ColorHelper.Argb.getBlue(color)/255f, 1);

                drawContext.enableScissor(position.x + 40, position.y + 60 - (int)(32 * ultimate_colored_bar_percentage), position.x + 80, position.y + 60);
                drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/ultimate_hexagon.png"), position.x + 40, position.y + 28, 0, 0, 40, 32, 40, 32);
                drawContext.disableScissor();

                last_ultimate_percent = percent;

                drawContext.setShaderColor(1, 1, 1, 1);

                RenderSystem.disableBlend();

                if (ultimate_white_bar_percentage - ultimate_colored_bar_percentage < 0.001) {
                    ultimate_colored_bar_percentage = ultimate_white_bar_percentage;
                }

                if (player.hasStatusEffect(ElementalBattleStatusEffects.SPELL_LOCK_EFFECT)) {
                    RenderSystem.enableBlend();
                    drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/locked_spell_display.png"), position.x, position.y, 0, 0, 120, 88, 120, 88);
                    RenderSystem.disableBlend();
                }
            }
        }));
    }

    private static void renderIcon(String slot, MagicStaffItem staff, DrawContext drawContext) {
        int x = position.x;
        int y = position.y;
        Spell spell = null;
        String key = "";

        switch (slot) {
            case "main":
                spell = staff.getUseSpell(MinecraftClient.getInstance().player);
                x += 24;
                y += 8;
                key = KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.useKey).getLocalizedText().getString();
                break;
            case "shield":
                spell = staff.getShieldSpell(MinecraftClient.getInstance().player);
                x += 72;
                y += 8;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.shield).getLocalizedText().getString();
                break;
            case "dash":
                spell = staff.getDashSpell(MinecraftClient.getInstance().player);
                x += 14;
                y += 33;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.dash).getLocalizedText().getString();
                break;
            case "areaAttack":
                spell = staff.getAreaAttackSpell(MinecraftClient.getInstance().player);
                x += 82;
                y += 33;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.areaAttack).getLocalizedText().getString();
                break;
            case "special":
                spell = staff.getSpecialSpell(MinecraftClient.getInstance().player);
                x += 24;
                y += 58;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.special).getLocalizedText().getString();
                break;
            case "ultimate":
                spell = staff.getUltimateSpell(MinecraftClient.getInstance().player);
                x += 72;
                y += 58;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.ultimate).getLocalizedText().getString();
                break;
        }

        if (spell != null) {
            // Turns values like "Right Mouse" into "RM". Really, it'd be better to do a dictionary but I don't feel like it rn.
            if (key.length() > 1) {
                String[] splitKey = key.split(" ");
                key = "";
                for (String index : splitKey) {
                    key += index.substring(0, 1);
                }
            }

            // Main spellComponent icon.
            drawContext.drawTexture(spell.getIcon(), x, y, 0, 0, 24, 22, 24, 22);

            // Renders overlay if the spellComponent is disabled.
            RenderSystem.enableBlend();

            if (!spell.canCast(MinecraftClient.getInstance().world, MinecraftClient.getInstance().player)) {
                drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/disabled.png"), x, y, 0, 0, 24, 22, 24, 22);
            }

            // Renders cooldown overlay.
            SpellCooldownManager playerSpellCooldownManager = MinecraftClient.getInstance().player.getAttachedOrCreate(SpellCooldownManager.SPELL_COOLDOWN_MANAGER_ATTACHMENT);
            if (playerSpellCooldownManager.onCooldown(spell)) {
                drawContext.enableScissor(x, y + (int) (22 * (1 - playerSpellCooldownManager.percentageLeft(spell))), x + 24, y + 22);
                drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/on_cooldown.png"), x, y, 0, 0, 24, 22, 24, 22);
                drawContext.disableScissor();
            }

            if (spell instanceof EmpoweredSpell empoweredSpell && empoweredSpell.isEmpowered(MinecraftClient.getInstance().player)) {
                drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/empowered.png"), x, y, 0, 0, 24, 22, 24, 22);
            }

            RenderSystem.disableBlend();

            drawContext.drawText(MinecraftClient.getInstance().textRenderer, key, x, y,0xFFFFFFFF, true);
        }
    }
}
