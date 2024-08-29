package io.github.mishkis.elemental_battle.spells;

import io.github.mishkis.elemental_battle.item.armor.AeromancersRobesItem;
import io.github.mishkis.elemental_battle.item.armor.CryomancersRobesItem;
import io.github.mishkis.elemental_battle.item.armor.PyromancersRobesItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;

public enum SpellElement implements StringIdentifiable {
    FLAME("flame", Formatting.RED),
    FROST("frost", Formatting.BLUE),
    AIR("air", Formatting.AQUA);

    private final String name;
    private final Formatting color;

    private SpellElement(final String name, final Formatting color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public Formatting getColor() {
        return this.color;
    }
}
