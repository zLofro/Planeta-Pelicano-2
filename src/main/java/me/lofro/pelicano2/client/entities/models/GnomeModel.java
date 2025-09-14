package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.GnomeEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GnomeModel extends GeoModel<GnomeEntity> {
    @Override
    public Identifier getModelResource(GnomeEntity gnomeEntity) {
        return new Identifier(ModConstants.MOD_ID, "geo/entity/gnome.geo.json");
    }

    @Override
    public Identifier getTextureResource(GnomeEntity gnomeEntity) {
        return new Identifier(ModConstants.MOD_ID, "textures/entity/gnome.png");
    }

    @Override
    public Identifier getAnimationResource(GnomeEntity gnomeEntity) {
        return new Identifier(ModConstants.MOD_ID, "animations/entity/gnome.animation.json");
    }

    @Override
    public void setCustomAnimations(GnomeEntity animatable, long instanceId, AnimationState<GnomeEntity> state) {
        super.setCustomAnimations(animatable, instanceId, state);

        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData entityData = state.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null && entityData != null) {
            if(!animatable.isInSneakingPose()) head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }
}
