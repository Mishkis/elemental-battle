package io.github.mishkis.elemental_battle.spells;

import net.minecraft.util.StringIdentifiable;

public enum SpellElement implements StringIdentifiable {
    FLAME("flame"),
    FROST("frost"),
    AIR("air");

    private final String name;

    private SpellElement(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
