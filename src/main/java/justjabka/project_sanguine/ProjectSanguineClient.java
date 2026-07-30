package justjabka.project_sanguine;

import justjabka.project_sanguine.registries.client.ProjectSanguineEntityRenderers;
import justjabka.project_sanguine.rendering.ProjectSanguineHud;
import net.fabricmc.api.ClientModInitializer;

public class ProjectSanguineClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ProjectSanguineHud.initialize();
        ProjectSanguineEntityRenderers.initialize();
    }
}
