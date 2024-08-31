package io.github.mishkis.elemental_battle.rendering;

import io.github.mishkis.elemental_battle.spells.Spell;
import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TooltipSpellComponent implements TooltipComponent {
    private final Spell spell;
    private final int color;
    private final StringVisitable description;

    public TooltipSpellComponent(TooltipSpellData tooltipSpellData) {
        this.spell = tooltipSpellData.spell();

        this.color = spell.getElement().getColor();
        this.description = StringVisitable.styled(("   " + spell.getDescription() + " - Codex Elementum"), Style.EMPTY.withColor(color).withItalic(true));
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        context.drawText(textRenderer, Text.literal(spell.getSpellName()).formatted(Formatting.BOLD), x, y, color, true);

        context.fill(x + this.getWidth(textRenderer) - 18, y, x + this.getWidth(textRenderer), y + 18, color);
        context.drawTexture(spell.getIcon(), x + this.getWidth(textRenderer) - 17, y + 1, 0, 0, 16, 16, 16, 16);

        y += textRenderer.fontHeight + 1;
        context.drawText(textRenderer, Text.literal(StringUtil.capitalize(spell.getElement().toString()) + " Element"), x, y, color, true);

        y += textRenderer.fontHeight + 1;

        context.drawHorizontalLine(x, x + getWidth(textRenderer), y, color);

        int movingX = x + getWidth(textRenderer) / 3 - getWidth(textRenderer) / 6;
        y += 5;
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Damage"), movingX, y, color);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(Float.toString(spell.getDamage())), movingX, y + textRenderer.fontHeight, color);

        movingX += getWidth(textRenderer) / 3;
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Uptime"), movingX, y, color);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(Math.round(spell.getUptime() / 2f)/10f + "s"), movingX, y + textRenderer.fontHeight, color);

        movingX += getWidth(textRenderer) / 3;
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Cooldown"), movingX, y, color);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(Math.round(spell.getCooldown() / 2f)/10f + "s"), movingX, y + textRenderer.fontHeight, color);

        y += textRenderer.fontHeight * 2 + 1;

        context.drawHorizontalLine(x, x + getWidth(textRenderer), y, color);

        y += 5;

        // Height of 49 at this point.
        context.drawTextWrapped(textRenderer, description, x + 2, y, this.getWidth(textRenderer) - 2, color);
    }

    @Override
    public int getHeight() {
        return 49 + MinecraftClient.getInstance().textRenderer.getWrappedLinesHeight(description, this.getWidth(MinecraftClient.getInstance().textRenderer) - 2);
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 138;
    }
}
