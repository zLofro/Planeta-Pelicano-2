package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.VoidFountainModel;
import me.lofro.pelicano2.server.entity.entities.VoidFountainEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VoidFountainRenderer extends GeoEntityRenderer<VoidFountainEntity> {

    public VoidFountainRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new VoidFountainModel());
    }

}
