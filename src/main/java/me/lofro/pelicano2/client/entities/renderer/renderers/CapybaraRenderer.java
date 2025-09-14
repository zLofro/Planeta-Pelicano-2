package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.CapybaraModel;
import me.lofro.pelicano2.server.entity.entities.CapybaraEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CapybaraRenderer extends GeoEntityRenderer<CapybaraEntity> {

    public CapybaraRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new CapybaraModel());
    }

}
