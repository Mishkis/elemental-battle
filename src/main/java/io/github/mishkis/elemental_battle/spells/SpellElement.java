package io.github.mishkis.elemental_battle.spells;

import net.minecraft.util.StringIdentifiable;

public enum SpellElement implements StringIdentifiable {
    FLAME("flame", 0xFFc81b1b),
    FROST("frost", 0xFF6168bb),
    AIR("air", 0xFFe0ecf6);

    private final String name;
    private final int color;

    SpellElement(final String name, final int color) {
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

    public int getColor() {
        return this.color;
    }
}
