package justjabka.project_sanguine.rendering.entity;

import justjabka.project_sanguine.contents.entity.Necrophagia;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

// TODO: Add actual model
public class NecrophagiaRenderer extends EntityRenderer<Necrophagia, NecrophagiaRenderState> {

    public NecrophagiaRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public NecrophagiaRenderState createRenderState() {
        return new NecrophagiaRenderState();
    }
}