package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.GuideMissionsEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class GuideMissionsModel extends GeoModel<GuideMissionsEntity> {

    @Override
    public Identifier getModelResource(GuideMissionsEntity guideEntity) {
        return new Identifier(ModConstants.MOD_ID, "geo/entity/guide.geo.json");
    }

    @Override
    public Identifier getTextureResource(GuideMissionsEntity guideEntity) {
        return new Identifier(ModConstants.MOD_ID, "textures/entity/guide.png");
    }

    @Override
    public Identifier getAnimationResource(GuideMissionsEntity guideEntity) {
        return new Identifier(ModConstants.MOD_ID, "animations/entity/guide.animation.json");
    }

}
