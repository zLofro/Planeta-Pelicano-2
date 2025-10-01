package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.NicteMissionsEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class NicteMissionsModel extends GeoModel<NicteMissionsEntity> {

    @Override
    public Identifier getModelResource(NicteMissionsEntity nicteEntity) {
        return new Identifier(ModConstants.MOD_ID, "geo/entity/nicte.geo.json");
    }

    @Override
    public Identifier getTextureResource(NicteMissionsEntity nicteEntity) {
        return new Identifier(ModConstants.MOD_ID, "textures/entity/nicte.png");
    }

    @Override
    public Identifier getAnimationResource(NicteMissionsEntity nicteEntity) {
        return new Identifier(ModConstants.MOD_ID, "animations/entity/nicte.animation.json");
    }

}
