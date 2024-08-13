package io.github.mishkis.elemental_battle.entity;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.flame_staff.ConeOfFireEntity;
import io.github.mishkis.elemental_battle.entity.flame_staff.FlamingDashEntity;
import io.github.mishkis.elemental_battle.entity.frost_staff.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ElementalBattleEntities {
    // Frost Magic
    public static final EntityType<IceTargetEntity> ICE_TARGET =
            register(
                    "ice_target",
                    EntityType.Builder.create(IceTargetEntity::new, SpawnGroup.MISC)
            );
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
    public static final EntityType<ShatteringWallEntity> SHATTERING_WALL =
            register(
                    "shattering_wall",
                    EntityType.Builder.create(ShatteringWallEntity::new, SpawnGroup.MISC)
                            .dimensions(2F, 2.5F)
            );
    public static final EntityType<FrozenSlideEntity> FROZEN_SLIDE =
            register(
                    "frozen_slide",
                    EntityType.Builder.create(FrozenSlideEntity::new, SpawnGroup.MISC)
                            .dimensions(2F, 2.5F)
            );

    // Flame Magic
    public static final EntityType<ConeOfFireEntity> CONE_OF_FIRE =
            register(
                    "cone_of_fire",
                    EntityType.Builder.create(ConeOfFireEntity::new, SpawnGroup.MISC)
                            .dimensions(1F, 1F)
            );
    public static final EntityType<FlamingDashEntity> FLAMING_DASH =
            register(
                    "flaming_dash",
                    EntityType.Builder.create(FlamingDashEntity::new, SpawnGroup.MISC)
                            .dimensions(2F, 2.5F)
            );

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(ElementalBattle.MOD_ID, id), type.build(id));
    }

    public static void initialize() {}
}
