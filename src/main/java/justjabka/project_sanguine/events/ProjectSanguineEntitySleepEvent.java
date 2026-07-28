package justjabka.project_sanguine.events;

import justjabka.project_sanguine.managers.SanityManager;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class ProjectSanguineEntitySleepEvent {
    public static void register() {
        EntitySleepEvents.STOP_SLEEPING.register(
                ProjectSanguineEntitySleepEvent::restoreSanityAfterSleep
        );
    }

    private static void restoreSanityAfterSleep(LivingEntity entity, BlockPos sleepingPos) {
        if (!(entity instanceof ServerPlayer player)) return;
        if (!player.isSleepingLongEnough()) return;

        SanityManager.restoreSanityAfterSleep(player);
    }
}
