package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.GnomeBossModel;
import me.lofro.pelicano2.server.entity.entities.GnomeBossEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GnomeBossRenderer extends GeoEntityRenderer<GnomeBossEntity> {

    public GnomeBossRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new GnomeBossModel());
    }

}
