package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.CapybaraFarmerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CapybaraFarmerModel extends GeoModel<CapybaraFarmerEntity> {

    @Override
    public Identifier getModelResource(CapybaraFarmerEntity capybaraFarmerEntity) {
        return new Identifier(ModConstants.MOD_ID, "geo/entity/capybara2.geo.json");
    }

    @Override
    public Identifier getTextureResource(CapybaraFarmerEntity capybaraFarmerEntity) {
        return new Identifier(ModConstants.MOD_ID, "textures/entity/capybara2.png");
    }

    @Override
    public Identifier getAnimationResource(CapybaraFarmerEntity capybaraFarmerEntity) {
        return new Identifier(ModConstants.MOD_ID, "animations/entity/capybara2.animation.json");
    }

    @Override
    public void setCustomAnimations(CapybaraFarmerEntity animatable, long instanceId, AnimationState<CapybaraFarmerEntity> state) {
        super.setCustomAnimations(animatable, instanceId, state);

        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData entityData = state.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null && entityData != null) {
            head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }

}
