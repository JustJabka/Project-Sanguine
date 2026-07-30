package justjabka.project_sanguine.registries.client;

import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.rendering.entity.NecrophagiaRenderer;
import justjabka.project_sanguine.registries.ProjectSanguineEntityTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class ProjectSanguineEntityRenderers {
    public static void initialize() {
        ProjectSanguine.LOGGER.info("Initializing entity renderers");
        EntityRenderers.register(ProjectSanguineEntityTypes.NECROPHAGIA, NecrophagiaRenderer::new);
    }
}
