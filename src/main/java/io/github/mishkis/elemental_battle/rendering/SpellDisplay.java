package io.github.mishkis.elemental_battle.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.item.MagicStaffItem;
import io.github.mishkis.elemental_battle.network.ElementalBattleNetworkClient;
import io.github.mishkis.elemental_battle.spells.Spell;
import io.github.mishkis.elemental_battle.spells.SpellCooldownManager;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
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
        String key = "";

        switch (slot) {
            case "main":
                spell = staff.getUseSpell();
                x = 3;
                y = drawContext.getScaledWindowHeight() - 39;
                key = KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.useKey).getLocalizedText().getString();
                break;
            case "shield":
                spell = staff.getShieldSpell();
                x = 23;
                y = drawContext.getScaledWindowHeight() - 39;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.shield).getLocalizedText().getString();
                break;
            case "dash":
                spell = staff.getDashSpell();
                x = 43;
                y = drawContext.getScaledWindowHeight() - 39;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.dash).getLocalizedText().getString();
                break;
            case "areaAttack":
                spell = staff.getAreaAttackSpell();
                x = 3;
                y = drawContext.getScaledWindowHeight() - 19;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.areaAttack).getLocalizedText().getString();
                break;
            case "special":
                spell = staff.getSpecialSpell();
                x = 23;
                y = drawContext.getScaledWindowHeight() - 19;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.special).getLocalizedText().getString();
                break;
            case "ultimate":
                spell = staff.getUltimateSpell();
                x = 43;
                y = drawContext.getScaledWindowHeight() - 19;
                key = KeyBindingHelper.getBoundKeyOf(ElementalBattleNetworkClient.ultimate).getLocalizedText().getString();
                break;
        }

        if (spell == null) {
            return;
        }

        // Turns values like "Right Mouse" into "RM". Really, it'd be better to do a dictionary but I don't feel like it rn.
        String[] splitKey = key.split(" ");
        key = "";
        for (int i = 0; i < splitKey.length; i++) {
            key += splitKey[i].substring(0, 1);
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

            drawContext.drawText(MinecraftClient.getInstance().textRenderer, key, x, y + 8,0xFFFFFFFF, true);

            RenderSystem.disableBlend();
        }
    }
}
