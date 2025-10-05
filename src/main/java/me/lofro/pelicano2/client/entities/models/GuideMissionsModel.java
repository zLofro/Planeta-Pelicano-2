package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.client.ModClient;
import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.GuideMissionsEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class GuideMissionsModel extends GeoModel<GuideMissionsEntity> {

    @Override
    public Identifier getModelResource(GuideMissionsEntity guideEntity) {
        if (ModClient.getClientMissionData().getAssignedMissions().contains(guideEntity.getMission())) {
            return new Identifier(ModConstants.MOD_ID, "geo/entity/guide.geo.json");
        } else {
            return new Identifier(ModConstants.MOD_ID, "geo/entity/guide_npc.geo.json");
        }
    }

    @Override
    public Identifier getTextureResource(GuideMissionsEntity guideEntity) {
        if (ModClient.getClientMissionData().getAssignedMissions().contains(guideEntity.getMission())) {
            return new Identifier(ModConstants.MOD_ID, "textures/entity/guide.png");
        } else {
            return new Identifier(ModConstants.MOD_ID, "textures/entity/guide_npc.png");
        }
    }

    @Override
    public Identifier getAnimationResource(GuideMissionsEntity guideEntity) {
        if (ModClient.getClientMissionData().getAssignedMissions().contains(guideEntity.getMission())) {
            return new Identifier(ModConstants.MOD_ID, "animations/entity/guide.animation.json");
        } else {
            return new Identifier(ModConstants.MOD_ID, "animations/entity/guide_npc.animation.json");
        }
    }

}
