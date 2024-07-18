package io.github.mishkis.elemental_battle.entity;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.frost_staff.IcicleBallEntity;
import io.github.mishkis.elemental_battle.entity.frost_staff.IcicleEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ElementalBattleEntities {
    public static final EntityType<IcicleBallEntity> ICICLE_BALL =
            register(
                    "icicle_ball",
                    EntityType.Builder.create(IcicleBallEntity::new, SpawnGroup.MISC)
                            .dimensions(0.5F, 0.5F)
            );
    public static final EntityType<IcicleEntity> ICICLE =
            register(
                    "icicle",
                    EntityType.Builder.create(IcicleEntity::new, SpawnGroup.MISC)
                            .dimensions(0.5F, 0.5F)
            );

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(ElementalBattle.MOD_ID, id), type.build(id));
    }

    public static void initialize() {}
}
