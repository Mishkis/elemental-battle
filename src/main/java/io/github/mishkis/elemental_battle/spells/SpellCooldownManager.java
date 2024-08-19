package io.github.mishkis.elemental_battle.spells;

import com.google.common.collect.Maps;
import io.github.mishkis.elemental_battle.ElementalBattle;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.util.Identifier;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

public class SpellCooldownManager {
    private final Map<Spell, Integer> cooldownMap = Maps.<Spell, Integer>newHashMap();
    private int tick = 0;

    public static final AttachmentType<SpellCooldownManager> SPELL_COOLDOWN_MANAGER_ATTACHMENT = AttachmentRegistry.createDefaulted(Identifier.of(ElementalBattle.MOD_ID, "spell_cooldown_manager_attachment"), SpellCooldownManager::new);

    public void tick() {
        if (cooldownMap.isEmpty()) {
            return;
        }
        // Only increase the tick if there is actually a reason to.
        tick++;

        Iterator<Map.Entry<Spell, Integer>> iterator = cooldownMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Spell, Integer> pair = iterator.next();

            if (pair.getValue() <= tick) {
                iterator.remove();
            }
        }
    }

    public void put(Spell spell, Integer cooldown) {
        cooldownMap.put(spell, tick + cooldown);
    }

    public int ticksLeft(Spell spell) {
        if (onCooldown(spell)) {
            return cooldownMap.get(spell) - tick;
        }
        return 0;
    }

    public boolean onCooldown(Spell spell) {
        return cooldownMap.containsKey(spell);
    }
}
