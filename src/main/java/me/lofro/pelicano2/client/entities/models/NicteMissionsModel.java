package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.client.ModClient;
import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.NicteMissionsEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class NicteMissionsModel extends GeoModel<NicteMissionsEntity> {

    @Override
    public Identifier getModelResource(NicteMissionsEntity nicteEntity) {
        if (ModClient.getClientMissionData().getAssignedMissions().contains(nicteEntity.getMission())) {
            return new Identifier(ModConstants.MOD_ID, "geo/entity/nicte.geo.json");
        } else {
            return new Identifier(ModConstants.MOD_ID, "geo/entity/nicte_npc.geo.json");
        }
    }

    @Override
    public Identifier getTextureResource(NicteMissionsEntity nicteEntity) {
        if (ModClient.getClientMissionData().getAssignedMissions().contains(nicteEntity.getMission())) {
            return new Identifier(ModConstants.MOD_ID, "textures/entity/nicte.png");
        } else {
            return new Identifier(ModConstants.MOD_ID, "textures/entity/nicte_npc.png");
        }
    }

    @Override
    public Identifier getAnimationResource(NicteMissionsEntity nicteEntity) {
        if (ModClient.getClientMissionData().getAssignedMissions().contains(nicteEntity.getMission())) {
            return new Identifier(ModConstants.MOD_ID, "animations/entity/nicte.animation.json");
        } else {
            return new Identifier(ModConstants.MOD_ID, "animations/entity/nicte_npc.animation.json");
        }
    }

    @Override
    public void setCustomAnimations(NicteMissionsEntity animatable, long instanceId, AnimationState<NicteMissionsEntity> state) {
        super.setCustomAnimations(animatable, instanceId, state);

        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData entityData = state.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null && entityData != null) {
            head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }

}
