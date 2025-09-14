package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.CustomCatEntity;
import me.lofro.pelicano2.server.entity.entities.GnomeBossEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GnomeBossModel extends GeoModel<GnomeBossEntity> {

    @Override
    public Identifier getModelResource(GnomeBossEntity gnomeBossEntity) {
        return new Identifier(ModConstants.MOD_ID, "geo/entity/gnome_boss.geo.json");
    }

    @Override
    public Identifier getTextureResource(GnomeBossEntity gnomeBossEntity) {
        return new Identifier(ModConstants.MOD_ID, "textures/entity/gnome_boss.png");
    }

    @Override
    public Identifier getAnimationResource(GnomeBossEntity gnomeBossEntity) {
        return new Identifier(ModConstants.MOD_ID, "animations/entity/gnome_boss.animation.json");
    }

    @Override
    public void setCustomAnimations(GnomeBossEntity animatable, long instanceId, AnimationState<GnomeBossEntity> state) {
        super.setCustomAnimations(animatable, instanceId, state);

        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData entityData = state.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null && entityData != null) {
            if(!animatable.isInSneakingPose()) head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }

}
