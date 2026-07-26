package justjabka.project_sanguine.events;

import justjabka.project_sanguine.contents.attachment.PlayerData;
import justjabka.project_sanguine.data.ProjectSanguineEntityTypeTagProvider;
import justjabka.project_sanguine.registries.ProjectSanguineAttachments;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;

public class ProjectSanguineServerTickEvent {
    private static final double SEARCH_RADIUS = 10;
    private static final float SKY_AURA = 0.1f;
    private static final float PET_AURA = 0.1f;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(ProjectSanguineServerTickEvent::handleSanityAura);
    }

    private static void handleSanityAura(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.tickCount % 20 != 0) continue;

            GameType gameMode = player.gameMode();
            if (gameMode == GameType.CREATIVE) continue;
            if (gameMode == GameType.SPECTATOR) continue;

            float aura = 0f;
            aura = handlePositiveSanityAura(player, aura);
            if (aura == 0f) continue;

            PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
            player.setAttached(
                    ProjectSanguineAttachments.PLAYER_DATA,
                    data.addSanity(aura)
            );
        }
    }

    private static float handlePositiveSanityAura(ServerPlayer player, float aura) {
        ServerLevel level = player.level();
        AABB aabb = player.getBoundingBox().inflate(SEARCH_RADIUS);

        LivingEntity nearestPet = level.getNearestEntity(
                ProjectSanguineEntityTypeTagProvider.INCREASES_SANITY,
                TargetingConditions.forNonCombat(),
                player,
                player.getX(),
                player.getY(),
                player.getZ(),
                aabb
        );

        boolean canSeeSky = level.canSeeSky(player.getOnPos());
        boolean hasNearestPet = nearestPet != null;

        if (canSeeSky) aura += SKY_AURA;
        if (hasNearestPet) aura += PET_AURA;

        return aura;
    }
}
