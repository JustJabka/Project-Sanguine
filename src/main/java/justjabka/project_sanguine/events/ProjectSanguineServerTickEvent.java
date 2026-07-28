package justjabka.project_sanguine.events;

import justjabka.project_sanguine.contents.attachment.PlayerData;
import justjabka.project_sanguine.managers.SanityManager;
import justjabka.project_sanguine.registries.ProjectSanguineAttachments;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySelector;

public class ProjectSanguineServerTickEvent {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(ProjectSanguineServerTickEvent::handleSanityAura);
    }

    public static void handleSanityAura(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player)) continue;

            float auraChange = 0f;
            if (player.tickCount % 20 == 0) auraChange += SanityManager.getPassiveAura(player);
            if (player.tickCount % 60 == 0) auraChange += SanityManager.useSanityProvider(player);

            // Update sanity
            if (auraChange == 0f) continue;

            PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
            player.setAttached(
                    ProjectSanguineAttachments.PLAYER_DATA,
                    data.addSanity(player, auraChange)
            );
        }
    }
}
