package io.github.mishkis.elemental_battle.entity;

import io.github.mishkis.elemental_battle.ElementalBattle;
import io.github.mishkis.elemental_battle.entity.air_staff.*;
import io.github.mishkis.elemental_battle.entity.flame_staff.*;
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
    public static final EntityType<ChillingAirEntity> CHILLING_AIR =
            register(
                    "chilling_air",
                    EntityType.Builder.create(ChillingAirEntity::new, SpawnGroup.MISC)
                            .dimensions(5.5F, 1F)
            );
    public static final EntityType<FrozenSolidEntity> FROZEN_SOLID =
            register(
                    "frozen_solid",
                    EntityType.Builder.create(FrozenSolidEntity::new, SpawnGroup.MISC)
                            .dimensions(2F, 2.2F)
            );

    // Flame Magic
    public static final EntityType<ConeOfFireEntity> CONE_OF_FIRE =
            register(
                    "cone_of_fire",
                    EntityType.Builder.create(ConeOfFireEntity::new, SpawnGroup.MISC)
                            .dimensions(1F, 1F)
            );
    public static final EntityType<WallOfFireEntity> WALL_OF_FIRE =
            register(
                    "wall_of_fire",
                    EntityType.Builder.create(WallOfFireEntity::new, SpawnGroup.MISC)
                            .dimensions(2F, 2.5F)
            );
    public static final EntityType<FlamingDashEntity> FLAMING_DASH =
            register(
                    "flaming_dash",
                    EntityType.Builder.create(FlamingDashEntity::new, SpawnGroup.MISC)
                            .dimensions(2F, 2.5F)
            );
    public static final EntityType<FlameVortexEntity> FLAME_VORTEX =
            register(
                    "flame_vortex",
                    EntityType.Builder.create(FlameVortexEntity::new, SpawnGroup.MISC)
                            .dimensions(6F, 1F)
            );
    public static final EntityType<FireyGraspEntity> FIREY_GRASP =
            register(
                    "firey_grasp",
                    EntityType.Builder.create(FireyGraspEntity::new, SpawnGroup.MISC)
                            .dimensions(1F, 1F)
            );

    // Air Magic
    public static final EntityType<GustEntity> GUST =
            register(
                    "gust",
                    EntityType.Builder.create(GustEntity::new, SpawnGroup.MISC)
                            .dimensions(1F, 1F)
            );
    public static final EntityType<BlowBackEntity> BLOW_BACK =
            register(
                    "blow_back",
                    EntityType.Builder.create(BlowBackEntity::new, SpawnGroup.MISC)
                            .dimensions(2F, 2.5F)
            );
    public static final EntityType<DoubleDashEntity> DOUBLE_DASH =
            register(
                    "double_dash",
                    EntityType.Builder.create(DoubleDashEntity::new, SpawnGroup.MISC)
                            .dimensions(2F, 2.5F)
            );
    public static final EntityType<AirLiftEntity> AIR_LIFT =
            register(
                    "air_lift",
                    EntityType.Builder.create(AirLiftEntity::new, SpawnGroup.MISC)
                            .dimensions(6F, 3F)
            );

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(ElementalBattle.MOD_ID, id), type.build(id));
    }

    public static void initialize() {}
}
