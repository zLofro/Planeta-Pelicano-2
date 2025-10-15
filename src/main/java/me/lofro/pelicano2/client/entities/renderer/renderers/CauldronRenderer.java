package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.CauldronModel;
import me.lofro.pelicano2.server.entity.entities.CauldronEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CauldronRenderer extends GeoEntityRenderer<CauldronEntity> {

    public CauldronRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new CauldronModel());
    }

}
