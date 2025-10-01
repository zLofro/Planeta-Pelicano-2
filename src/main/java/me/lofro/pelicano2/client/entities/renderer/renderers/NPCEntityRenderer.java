package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.client.entities.models.NPCEntityModel;
import me.lofro.pelicano2.server.entity.entities.NPCEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class NPCEntityRenderer extends MobEntityRenderer<NPCEntity, NPCEntityModel<NPCEntity>> {

    public NPCEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new NPCEntityModel<>(context.getPart(NPCEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public Identifier getTexture(NPCEntity entity) {
        return entity.getTexture();
    }

}
