package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.CapybaraEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class CapybaraModel extends GeoModel<CapybaraEntity> {
    @Override
    public Identifier getModelResource(CapybaraEntity capybaraEntity) {
        return new Identifier(ModConstants.MOD_ID, "geo/entity/capybara.geo.json");
    }

    @Override
    public Identifier getTextureResource(CapybaraEntity capybaraEntity) {
        return new Identifier(ModConstants.MOD_ID, "textures/entity/capybara.png");
    }

    @Override
    public Identifier getAnimationResource(CapybaraEntity capybaraEntity) {
        return new Identifier(ModConstants.MOD_ID, "animations/entity/capybara.animation.json");
    }
}
