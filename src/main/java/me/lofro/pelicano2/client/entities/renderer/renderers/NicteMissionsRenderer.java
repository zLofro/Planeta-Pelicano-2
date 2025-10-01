package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.NicteMissionsModel;
import me.lofro.pelicano2.server.entity.entities.NicteMissionsEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NicteMissionsRenderer extends GeoEntityRenderer<NicteMissionsEntity> {

    public NicteMissionsRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new NicteMissionsModel());
    }

}
