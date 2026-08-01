package justjabka.project_sanguine.rendering.entity;

import justjabka.project_sanguine.contents.entity.Arachnophobia;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

// TODO: Add actual model
@Environment(EnvType.CLIENT)
public class ArachnophobiaRenderer extends EntityRenderer<Arachnophobia, ArachnophobiaRenderState> {

    public ArachnophobiaRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ArachnophobiaRenderState createRenderState() {
        return new ArachnophobiaRenderState();
    }
}