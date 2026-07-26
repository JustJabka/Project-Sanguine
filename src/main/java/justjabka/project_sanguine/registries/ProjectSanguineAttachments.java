package justjabka.project_sanguine.registries;

import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.contents.attachment.PlayerData;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class ProjectSanguineAttachments {
    public static final AttachmentType<PlayerData> PLAYER_DATA = AttachmentRegistry.create(
            ProjectSanguine.id("player_data"),
            builder -> builder
                    .initializer(() -> PlayerData.DEFAULT)
                    .persistent(PlayerData.CODEC)
                    .syncWith(PlayerData.STREAM, AttachmentSyncPredicate.targetOnly())
    );

    public static void initialize() {
        ProjectSanguine.LOGGER.info("Initializing Attachments");
    }
}
