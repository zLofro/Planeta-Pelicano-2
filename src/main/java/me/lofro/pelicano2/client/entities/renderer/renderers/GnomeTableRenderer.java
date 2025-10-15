package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.GnomeTableModel;
import me.lofro.pelicano2.server.entity.entities.GnomeTableEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GnomeTableRenderer extends GeoEntityRenderer<GnomeTableEntity> {

    public GnomeTableRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new GnomeTableModel());
    }

}
