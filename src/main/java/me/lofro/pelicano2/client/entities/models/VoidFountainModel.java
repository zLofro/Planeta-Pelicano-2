package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.VoidFountainEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class VoidFountainModel extends GeoModel<VoidFountainEntity> {

    @Override
    public Identifier getModelResource(VoidFountainEntity voidFountainEntity) {
        return new Identifier(ModConstants.MOD_ID, "geo/entity/void_fountain.geo.json");
    }

    @Override
    public Identifier getTextureResource(VoidFountainEntity voidFountainEntity) {
        return new Identifier(ModConstants.MOD_ID, "textures/entity/void_fountain.png");
    }

    @Override
    public Identifier getAnimationResource(VoidFountainEntity voidFountainEntity) {
        return new Identifier(ModConstants.MOD_ID, "animations/entity/void_fountain.animation.json");
    }

}
