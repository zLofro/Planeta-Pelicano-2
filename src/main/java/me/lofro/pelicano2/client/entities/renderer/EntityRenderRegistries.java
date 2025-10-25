package me.lofro.pelicano2.client.entities.renderer;

import me.lofro.pelicano2.client.entities.renderer.renderers.*;
import me.lofro.pelicano2.server.entity.ModEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class EntityRenderRegistries {

    public static void register() {
        EntityRendererRegistry.register(ModEntities.CUSTOM_CAT, CustomCatRenderer::new);
        EntityRendererRegistry.register(ModEntities.CAPYBARA, CapybaraRenderer::new);
        EntityRendererRegistry.register(ModEntities.CAPYBARA_FARMER, CapybaraFarmerRenderer::new);
        EntityRendererRegistry.register(ModEntities.NICTE, NicteRenderer::new);
        EntityRendererRegistry.register(ModEntities.NICTE_MISSIONS, NicteMissionsRenderer::new);
        EntityRendererRegistry.register(ModEntities.GNOME, GnomeRenderer::new);
        EntityRendererRegistry.register(ModEntities.GUIDE, GuideRenderer::new);
        EntityRendererRegistry.register(ModEntities.GUIDE_MISSIONS, GuideMissionsRenderer::new);
        EntityRendererRegistry.register(ModEntities.GNOME_BOSS, GnomeBossRenderer::new);
        EntityRendererRegistry.register(ModEntities.NPC_ENTITY, NPCEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.GNOME_TABLE, GnomeTableRenderer::new);
        EntityRendererRegistry.register(ModEntities.VOID_FOUNTAIN, VoidFountainRenderer::new);
        EntityRendererRegistry.register(ModEntities.CAULDRON_ENTITY, CauldronRenderer::new);
        EntityRendererRegistry.register(ModEntities.ANGRY_SALMON, AngrySalmonRenderer::new);
    }

}
