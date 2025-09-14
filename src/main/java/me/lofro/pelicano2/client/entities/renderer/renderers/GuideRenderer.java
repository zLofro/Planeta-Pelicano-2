package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.GuideModel;
import me.lofro.pelicano2.server.entity.entities.GuideEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GuideRenderer extends GeoEntityRenderer<GuideEntity> {

    public GuideRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new GuideModel());
    }

}
