package me.lofro.pelicano2.server.entity;

import me.lofro.pelicano2.server.entity.entities.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class EntityRegistries {

    public static void register() {
        FabricDefaultAttributeRegistry.register(ModEntities.CUSTOM_CAT, CustomCatEntity.createCustomCatAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.CAPYBARA, CapybaraEntity.createCapybaraAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.CAPYBARA_FARMER, CapybaraFarmerEntity.createCapybaraAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.NICTE, NicteEntity.createNicteAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.NICTE_MISSIONS, NicteMissionsEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.GNOME, GnomeEntity.createGnomeAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.GUIDE, GuideEntity.createGuideAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.GUIDE_MISSIONS, GuideMissionsEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.GNOME_BOSS, GnomeBossEntity.createGnomeBossAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.NPC_ENTITY, NPCEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.GNOME_TABLE, GnomeTableEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.VOID_FOUNTAIN, VoidFountainEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.CAULDRON_ENTITY, CauldronEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.ANGRY_SALMON, AngrySalmonEntity.createAttributes());
    }

}
