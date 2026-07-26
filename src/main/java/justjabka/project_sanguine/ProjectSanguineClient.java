package justjabka.project_sanguine;

import justjabka.project_sanguine.rendering.ProjectSanguineHud;
import net.fabricmc.api.ClientModInitializer;

public class ProjectSanguineClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ProjectSanguineHud.initialize();
    }
}
