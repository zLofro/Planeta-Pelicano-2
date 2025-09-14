package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.GnomeModel;
import me.lofro.pelicano2.server.entity.entities.GnomeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GnomeRenderer extends GeoEntityRenderer<GnomeEntity> {

    public GnomeRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new GnomeModel());
    }

}
