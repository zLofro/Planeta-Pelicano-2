package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.CauldronEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class CauldronModel extends GeoModel<CauldronEntity> {

    @Override
    public Identifier getModelResource(CauldronEntity cauldronEntity) {
        return new Identifier(ModConstants.MOD_ID, "geo/entity/cauldron.geo.json");
    }

    @Override
    public Identifier getTextureResource(CauldronEntity cauldronEntity) {
        return new Identifier(ModConstants.MOD_ID, "textures/entity/cauldron.png");
    }

    @Override
    public Identifier getAnimationResource(CauldronEntity cauldronEntity) {
        return new Identifier(ModConstants.MOD_ID, "animations/entity/cauldron.animation.json");
    }

}
