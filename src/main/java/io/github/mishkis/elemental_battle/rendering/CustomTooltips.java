package io.github.mishkis.elemental_battle.rendering;

import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

public class CustomTooltips {
    public static void initialize() {
        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof TooltipSpellData tooltipSpellData) {
                return new TooltipSpellComponent(tooltipSpellData);
            }

            return null;
        });
    }
}
