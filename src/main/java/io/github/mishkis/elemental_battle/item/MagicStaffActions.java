package io.github.mishkis.elemental_battle.item;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;

public enum MagicStaffActions implements StringIdentifiable {
    SHIELD(0, "shield"),
    DASH(1, "dash"),
    AREA_ATTACK(2, "area_attack"),
    SPECIAL(3, "special"),
    ULTIMATE(4, "ultimate");

    private final int id;
    private final String name;

    private static IntFunction<MagicStaffActions> ID_TO_VALUE_FUNCTION = ValueLists.createIdToValueFunction(MagicStaffActions::getId, MagicStaffActions.values(), ValueLists.OutOfBoundsHandling.WRAP);
    public static final PacketCodec<ByteBuf, MagicStaffActions> PACKET_CODEC = PacketCodecs.indexed(ID_TO_VALUE_FUNCTION, MagicStaffActions::getId);

    MagicStaffActions(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String asString() {
        return name;
    }
}
