package justjabka.project_sanguine;

import justjabka.project_sanguine.rendering.visual.InsanityPostEffectManager;
import justjabka.project_sanguine.rendering.ProjectSanguineHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;

public class ProjectSanguineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ProjectSanguineHud.initialize();

        LevelExtractionEvents.END_EXTRACTION.register(_ -> InsanityPostEffectManager.extractSanity());
        LevelRenderEvents.END_MAIN.register(_ -> InsanityPostEffectManager.renderSanityEffect());

    }
}
