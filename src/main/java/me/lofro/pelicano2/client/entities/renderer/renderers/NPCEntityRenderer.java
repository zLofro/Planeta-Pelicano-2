package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.NPCEntityModel;
import me.lofro.pelicano2.server.entity.entities.NPCEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NPCEntityRenderer extends GeoEntityRenderer<NPCEntity> {

    public NPCEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new NPCEntityModel());
    }

}
