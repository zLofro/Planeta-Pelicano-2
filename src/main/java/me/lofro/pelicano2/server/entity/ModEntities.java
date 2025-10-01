package me.lofro.pelicano2.server.entity;

import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<CustomCatEntity> CUSTOM_CAT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ModConstants.MOD_ID, "custom_cat"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER,CustomCatEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 0.6f))
                    .build()
    );

    public static final EntityType<CapybaraEntity> CAPYBARA = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ModConstants.MOD_ID, "capybara"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, CapybaraEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 0.8f))
                    .build()
    );

    public static final EntityType<NicteEntity> NICTE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ModConstants.MOD_ID, "nicte"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, NicteEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f, 2f))
                    .build()
    );

    public static final EntityType<NicteMissionsEntity> NICTE_MISSIONS = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ModConstants.MOD_ID, "nicte_missions"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, NicteMissionsEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f, 2f))
                    .build()
    );

    public static final EntityType<GnomeEntity> GNOME = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ModConstants.MOD_ID, "gnome"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GnomeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .build()
    );

    public static final EntityType<GuideEntity> GUIDE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ModConstants.MOD_ID, "guide"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GuideEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9f, 2.3f))
                    .build()
    );

    public static final EntityType<GnomeBossEntity> GNOME_BOSS = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ModConstants.MOD_ID, "gnome_boss"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GnomeBossEntity::new)
                    .dimensions(EntityDimensions.fixed(1f, 1.2f))
                    .build()
    );

    public static final EntityType<NPCEntity> NPC_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ModConstants.MOD_ID, "npc"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NPCEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f)) // tamaño como un jugador
                    .build()
    );

}
