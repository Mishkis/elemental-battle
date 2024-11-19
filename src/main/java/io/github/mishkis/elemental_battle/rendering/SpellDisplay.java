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
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;

public class SpellDisplay {
    public static final AttachmentType<Boolean> SPELL_DISPLAY_SHIELD_WARNING_ATTACHMENT = AttachmentRegistry.create(Identifier.of(ElementalBattle.MOD_ID, "spell_display_shield_warning_attachment"));

    private static final Vector2i position = new Vector2i(0, 0);

    private static double ultimate_white_bar_percentage = 0;
    private static double ultimate_colored_bar_percentage = 0;

    public static void initialize() {
        HudRenderCallback.EVENT.register(((drawContext, tickCounter) -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;

            if (player.getMainHandStack().getItem() instanceof MagicStaffItem staff) {
                position.x = drawContext.getScaledWindowWidth() - 388;
                position.y = drawContext.getScaledWindowHeight() - 62;

                RenderSystem.enableBlend();
                drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/spell_display.png"), position.x, position.y, 0, 0, 62, 62, 62, 62);
                RenderSystem.disableBlend();

                position.y += 20;

                renderIcon("main", staff, drawContext);
                renderIcon("shield", staff, drawContext);
                renderIcon("dash", staff, drawContext);
                renderIcon("areaAttack", staff, drawContext);
                renderIcon("special", staff, drawContext);
                renderIcon("ultimate", staff, drawContext);

                if (player.hasStatusEffect(ElementalBattleStatusEffects.SPELL_LOCK_EFFECT)) {
                    RenderSystem.enableBlend();
                    drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/locked_spell_display.png"), position.x, position.y, 0, 0, 62, 42, 62, 42);
                    RenderSystem.disableBlend();
                }

                if (player.getAttached(SPELL_DISPLAY_SHIELD_WARNING_ATTACHMENT) != null && !player.hasStatusEffect(ElementalBattleStatusEffects.SHIELD_EFFECT)) {
                    drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/shield_alert.png"), drawContext.getScaledWindowWidth()/2 - 17, drawContext.getScaledWindowHeight()/2 - 11, 1000, 0, 0, 34, 18, 34, 18);
                }

                // Draw ultimate bar
                ultimate_white_bar_percentage = MathHelper.lerp(0.5, ultimate_white_bar_percentage, player.getAttachedOrCreate(SpellUltimateManager.SPELL_ULTIMATE_MANAGER_ATTACHMENT).getPercent(staff.getElement()));
                drawContext.enableScissor(position.x + 3, position.y - 17, position.x + 7, position.y - 1);
                drawContext.fill(position.x + 3, position.y - 1, position.x + 3 + (int)(56 * ultimate_white_bar_percentage), position.y - 17, Colors.WHITE);
                drawContext.disableScissor();
                drawContext.fill(position.x + 3, position.y - 1, position.x + 3 + (int)(56 * ultimate_white_bar_percentage), position.y - 5, Colors.WHITE);

                ultimate_colored_bar_percentage = MathHelper.lerp(0.1, ultimate_colored_bar_percentage, player.getAttachedOrCreate(SpellUltimateManager.SPELL_ULTIMATE_MANAGER_ATTACHMENT).getPercent(staff.getElement()));
                drawContext.enableScissor(position.x + 3, position.y - 17, position.x + 7, position.y - 1);
                drawContext.fill(position.x + 3, position.y - 1, position.x + 3 + (int)(56 * ultimate_colored_bar_percentage), position.y - 17, staff.getElement().getColor());
                drawContext.disableScissor();
                drawContext.fill(position.x + 3, position.y - 1, position.x + 3 + (int)(56 * ultimate_colored_bar_percentage), position.y - 5, staff.getElement().getColor());

                if (ultimate_white_bar_percentage - ultimate_colored_bar_percentage < 0.001) {
                    ultimate_colored_bar_percentage = ultimate_white_bar_percentage;
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
                x += 3;
                y += 3;
                key = KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.useKey).getLocalizedText().getString();
                break;
            case "shield":
                spell = staff.getShieldSpell(MinecraftClient.getInstance().player);
                x += 23;
                y += 3;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.shield).getLocalizedText().getString();
                break;
            case "dash":
                spell = staff.getDashSpell(MinecraftClient.getInstance().player);
                x += 43;
                y += 3;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.dash).getLocalizedText().getString();
                break;
            case "areaAttack":
                spell = staff.getAreaAttackSpell(MinecraftClient.getInstance().player);
                x += 3;
                y += 23;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.areaAttack).getLocalizedText().getString();
                break;
            case "special":
                spell = staff.getSpecialSpell(MinecraftClient.getInstance().player);
                x += 23;
                y += 23;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.special).getLocalizedText().getString();
                break;
            case "ultimate":
                spell = staff.getUltimateSpell(MinecraftClient.getInstance().player);
                x += 43;
                y += 23;
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
            drawContext.drawTexture(spell.getIcon(), x, y, 0, 0, 16, 16, 16, 16);

            // Renders overlay if the spellComponent is disabled.
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

            drawContext.drawText(MinecraftClient.getInstance().textRenderer, key, x, y + 8,0xFFFFFFFF, true);

            RenderSystem.disableBlend();

            if (spell instanceof EmpoweredSpell empoweredSpell && empoweredSpell.isEmpowered(MinecraftClient.getInstance().player)) {
                drawContext.drawTexture(Identifier.of(ElementalBattle.MOD_ID, "textures/hud/empowered.png"), x - 2, y - 2, 0, 0, 20, 20, 20, 20);
            }
        }
    }
}
