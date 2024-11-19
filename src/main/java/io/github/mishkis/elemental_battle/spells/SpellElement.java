package io.github.mishkis.elemental_battle.spells;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;

public enum SpellElement implements StringIdentifiable {
    FLAME(0, "flame", 0xFFc81b1b),
    FROST(1, "frost", 0xFF6168bb),
    AIR(2, "air", 0xFFe0ecf6);

    private final int id;
    private final String name;
    private final int color;

    private static IntFunction<SpellElement> ID_TO_VALUE_FUNCTION = ValueLists.createIdToValueFunction(SpellElement::getId, SpellElement.values(), ValueLists.OutOfBoundsHandling.WRAP);
    public static final PacketCodec<ByteBuf, SpellElement> PACKET_CODEC = PacketCodecs.indexed(ID_TO_VALUE_FUNCTION, SpellElement::getId);

    SpellElement(int id, final String name, final int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public int getId() {
        return this.id;
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
