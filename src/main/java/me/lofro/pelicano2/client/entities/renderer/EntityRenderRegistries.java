package me.lofro.pelicano2.client.entities.renderer;

import me.lofro.pelicano2.client.entities.renderer.renderers.*;
import me.lofro.pelicano2.server.entity.ModEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class EntityRenderRegistries {

    public static void register() {
        EntityRendererRegistry.register(ModEntities.CUSTOM_CAT, CustomCatRenderer::new);
        EntityRendererRegistry.register(ModEntities.CAPYBARA, CapybaraRenderer::new);
        EntityRendererRegistry.register(ModEntities.NICTE, NicteRenderer::new);
        EntityRendererRegistry.register(ModEntities.NICTE_MISSIONS, NicteMissionsRenderer::new);
        EntityRendererRegistry.register(ModEntities.GNOME, GnomeRenderer::new);
        EntityRendererRegistry.register(ModEntities.GUIDE, GuideRenderer::new);
        EntityRendererRegistry.register(ModEntities.GNOME_BOSS, GnomeBossRenderer::new);
        EntityRendererRegistry.register(ModEntities.NPC_ENTITY, NPCEntityRenderer::new);
    }

}
