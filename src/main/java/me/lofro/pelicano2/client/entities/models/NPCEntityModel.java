package me.lofro.pelicano2.client.entities.models;

import me.lofro.pelicano2.server.entity.entities.NPCEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public class NPCEntityModel<T extends NPCEntity> extends PlayerEntityModel<T> {

    public static final EntityModelLayer LAYER_LOCATION = EntityModelLayers.PLAYER;

    public NPCEntityModel(ModelPart root) {
        super(root, false);
    }

}
