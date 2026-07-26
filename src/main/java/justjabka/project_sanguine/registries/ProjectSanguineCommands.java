package justjabka.project_sanguine.registries;

import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.contents.command.SanityCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ProjectSanguineCommands {
    public static void initialize() {
        ProjectSanguine.LOGGER.info("Initializing Commands");
        registerCommands();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            SanityCommand.register(dispatcher);
        });
    }
}
