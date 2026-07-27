package justjabka.project_sanguine;

import justjabka.project_sanguine.registries.*;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectSanguine implements ModInitializer {
	public static final String MOD_ID = "project_sanguine";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ProjectSanguineAttachments.initialize();
		ProjectSanguineCommands.initialize();
		ProjectSanguineEvents.initialize();
		ProjectSanguineComponents.initialize();
		ProjectSanguineItems.initialize();
		ProjectSanguineAttributes.initialize();
		ProjectSanguineEnvironmentAttributes.initialize();
		ProjectSanguineConsumeEffects.initialize();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
