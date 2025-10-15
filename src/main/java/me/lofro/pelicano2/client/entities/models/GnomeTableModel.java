package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.GnomeTableEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class GnomeTableModel extends GeoModel<GnomeTableEntity> {

    @Override
    public Identifier getModelResource(GnomeTableEntity gnomeTableEntity) {
        return new Identifier(ModConstants.MOD_ID, "geo/entity/gnomo_table.geo.json");
    }

    @Override
    public Identifier getTextureResource(GnomeTableEntity gnomeTableEntity) {
        return new Identifier(ModConstants.MOD_ID, "textures/entity/gnomo_table.png");
    }

    @Override
    public Identifier getAnimationResource(GnomeTableEntity gnomeTableEntity) {
        return new Identifier(ModConstants.MOD_ID, "animations/entity/gnomo_table.animation.json");
    }

}
