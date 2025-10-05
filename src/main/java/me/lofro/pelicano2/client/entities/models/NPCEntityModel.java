package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.client.ModClient;
import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.entity.entities.NPCEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class NPCEntityModel extends GeoModel<NPCEntity> {

    @Override
    public Identifier getModelResource(NPCEntity npcEntity) {
        if (ModClient.getClientMissionData().getAssignedMissions().contains(npcEntity.getMission())) {
            return new Identifier(ModConstants.MOD_ID, "geo/entity/npc.geo.json");
        } else {
            return new Identifier(ModConstants.MOD_ID, "geo/entity/npc_signed.geo.json");
        }
    }

    @Override
    public Identifier getTextureResource(NPCEntity npcEntity) {
        return npcEntity.getTexture();
    }

    @Override
    public Identifier getAnimationResource(NPCEntity npcEntity) {
        if (ModClient.getClientMissionData().getAssignedMissions().contains(npcEntity.getMission())) {
            return new Identifier(ModConstants.MOD_ID, "animations/entity/npc.animation.json");
        } else {
            return new Identifier(ModConstants.MOD_ID, "animations/entity/npc_signed.animation.json");
        }
    }

    @Override
    public void setCustomAnimations(NPCEntity animatable, long instanceId, AnimationState<NPCEntity> state) {
        super.setCustomAnimations(animatable, instanceId, state);

        CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData entityData = state.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null && entityData != null) {
            head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }

}
