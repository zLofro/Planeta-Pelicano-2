package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.CapybaraFarmerModel;
import me.lofro.pelicano2.server.entity.entities.CapybaraFarmerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CapybaraFarmerRenderer extends GeoEntityRenderer<CapybaraFarmerEntity> {

    public CapybaraFarmerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new CapybaraFarmerModel());
    }

}
