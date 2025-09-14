package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.NicteEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class NicteModel extends GeoModel<NicteEntity> {

    @Override
    public Identifier getModelResource(NicteEntity nicteEntity) {
        return new Identifier(ModConstants.MOD_ID, "geo/entity/nicte.geo.json");
    }

    @Override
    public Identifier getTextureResource(NicteEntity nicteEntity) {
        return new Identifier(ModConstants.MOD_ID, "textures/entity/nicte.png");
    }

    @Override
    public Identifier getAnimationResource(NicteEntity nicteEntity) {
        return new Identifier(ModConstants.MOD_ID, "animations/entity/nicte.animation.json");
    }

}
