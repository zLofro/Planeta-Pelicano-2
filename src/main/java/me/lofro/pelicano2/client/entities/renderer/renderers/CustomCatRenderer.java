package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.CustomCatModel;
import me.lofro.pelicano2.server.entity.entities.CustomCatEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CustomCatRenderer extends GeoEntityRenderer<CustomCatEntity> {

    public CustomCatRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new CustomCatModel());
    }

}
