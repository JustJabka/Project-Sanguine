package justjabka.project_sanguine.registries;

import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.events.ProjectSanguineEntityDeathEvent;
import justjabka.project_sanguine.events.ProjectSanguineEntitySleepEvent;
import justjabka.project_sanguine.events.ProjectSanguineServerTickEvent;

public class ProjectSanguineEvents {
    public static void initialize() {
        ProjectSanguine.LOGGER.info("Initializing Events");

        ProjectSanguineServerTickEvent.register();
        ProjectSanguineEntitySleepEvent.register();
        ProjectSanguineEntityDeathEvent.register();
    }
}
