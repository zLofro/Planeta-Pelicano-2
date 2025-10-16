package me.lofro.pelicano2.client.entities.renderer.renderers;

import me.lofro.pelicano2.server.entity.entities.AngrySalmonEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SalmonEntityModel;
import net.minecraft.util.Identifier;

public class AngrySalmonRenderer extends MobEntityRenderer<AngrySalmonEntity, SalmonEntityModel<AngrySalmonEntity>> {

    private static final Identifier TEXTURE = new Identifier("textures/entity/fish/salmon.png");

    public AngrySalmonRenderer(EntityRendererFactory.Context context) {
        super(context, new SalmonEntityModel<>(context.getPart(EntityModelLayers.SALMON)), 0.4f);
    }

    @Override
    public Identifier getTexture(AngrySalmonEntity entity) {
        return TEXTURE;
    }

}
