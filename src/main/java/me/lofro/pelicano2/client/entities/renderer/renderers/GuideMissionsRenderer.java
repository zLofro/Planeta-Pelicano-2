package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.GuideMissionsModel;
import me.lofro.pelicano2.server.entity.entities.GuideMissionsEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GuideMissionsRenderer extends GeoEntityRenderer<GuideMissionsEntity> {

    public GuideMissionsRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new GuideMissionsModel());
    }

}
