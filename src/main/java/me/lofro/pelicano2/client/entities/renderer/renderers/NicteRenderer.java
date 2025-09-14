package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.NicteModel;
import me.lofro.pelicano2.server.entity.entities.NicteEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NicteRenderer extends GeoEntityRenderer<NicteEntity> {

    public NicteRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new NicteModel());
    }

}
