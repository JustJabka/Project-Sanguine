package justjabka.project_sanguine.events;

import justjabka.project_sanguine.contents.attachment.PlayerData;
import justjabka.project_sanguine.contents.component.SanityProviderComponent;
import justjabka.project_sanguine.registries.ProjectSanguineAttachments;
import justjabka.project_sanguine.registries.ProjectSanguineComponents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.GameType;

import java.util.List;

public class ProjectSanguineServerTickEvent {
    // TODO: Move pet aura from entity tag to NBT `project_sanguine:sanity_aura`
    // TODO: Add Biome Attribute `project_sanguine:sanity_aura`
    private static final float SKY_AURA = 0.05f;

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
            aura = getSanityFromEnvironment(player, aura);
            aura = getSanityFromSanityProvider(player, aura);

            PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
            player.setAttached(
                    ProjectSanguineAttachments.PLAYER_DATA,
                    data.addSanity(aura)
            );
        }
    }

    private static float getSanityFromEnvironment(ServerPlayer player, float aura) {
        ServerLevel level = player.level();

        boolean canSeeSky = level.canSeeSky(player.blockPosition());

        if (canSeeSky) aura += SKY_AURA;

        return aura;
    }

    // Sanity Provider
    private static float getSanityFromSanityProvider(ServerPlayer player, float aura) {
        // Get all equipment with sanity provider component
        List<EquipmentSlot> slotsWithSanityProvider = EquipmentSlot.VALUES.stream()
                .filter(slot -> canSanityProviderUsing(player.getItemBySlot(slot), slot))
                .toList();

        if (slotsWithSanityProvider.isEmpty()) return aura;

        // Find item that need to be damaged
        EquipmentSlot slotToDamage = Util.getRandom(slotsWithSanityProvider, player.getRandom());
        ItemStack itemToDamage = player.getItemBySlot(slotToDamage);

        SanityProviderComponent sanityProvider = itemToDamage.get(ProjectSanguineComponents.SANITY_PROVIDER);

        // Provide sanity and damage item
        if (sanityProvider != null) {
            itemToDamage.hurtAndBreak(sanityProvider.itemDamagePerUse(), player, slotToDamage);
            aura += sanityProvider.sanityPerUse();
        }

        return aura;
    }

    private static boolean canSanityProviderUsing(final ItemStack itemStack, final EquipmentSlot slot) {
        if (!itemStack.has(ProjectSanguineComponents.SANITY_PROVIDER)) return false;

        Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
        return equippable != null && slot == equippable.slot() && !itemStack.nextDamageWillBreak();
    }
}
