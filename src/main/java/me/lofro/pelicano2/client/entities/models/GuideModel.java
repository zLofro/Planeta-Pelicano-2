package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.GuideEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GuideModel extends GeoModel<GuideEntity> {

    @Override
    public Identifier getModelResource(GuideEntity guideEntity) {
        return new Identifier(ModConstants.MOD_ID, "geo/entity/guide.geo.json");
    }

    @Override
    public Identifier getTextureResource(GuideEntity guideEntity) {
        return new Identifier(ModConstants.MOD_ID, "textures/entity/guide.png");
    }

    @Override
    public Identifier getAnimationResource(GuideEntity guideEntity) {
        return new Identifier(ModConstants.MOD_ID, "animations/entity/guide.animation.json");
    }

    @Override
    public void setCustomAnimations(GuideEntity animatable, long instanceId, AnimationState<GuideEntity> state) {
        super.setCustomAnimations(animatable, instanceId, state);

        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData entityData = state.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null && entityData != null) {
            head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }

}
