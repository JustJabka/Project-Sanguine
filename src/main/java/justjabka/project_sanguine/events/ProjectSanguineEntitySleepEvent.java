package justjabka.project_sanguine.events;

import justjabka.project_sanguine.contents.attachment.PlayerData;
import justjabka.project_sanguine.registries.ProjectSanguineAttachments;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class ProjectSanguineEntitySleepEvent {
    private static final float BONUS_SANITY_FROM_SLEEP = 15f;

    public static void register() {
        EntitySleepEvents.STOP_SLEEPING.register(
                ProjectSanguineEntitySleepEvent::restoreSanityAfterSleep
        );
    }

    private static void restoreSanityAfterSleep(LivingEntity entity, BlockPos sleepingPos) {
        if (!(entity instanceof ServerPlayer player)) return;
        if (!player.isSleepingLongEnough()) return;

        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
        player.setAttached(
                ProjectSanguineAttachments.PLAYER_DATA,
                data.addSanity(player, BONUS_SANITY_FROM_SLEEP)
        );
    }
}
