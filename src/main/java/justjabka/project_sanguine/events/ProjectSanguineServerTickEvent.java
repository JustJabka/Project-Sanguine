package justjabka.project_sanguine.events;

import justjabka.project_sanguine.contents.attachment.PlayerData;
import justjabka.project_sanguine.contents.component.SanityProviderComponent;
import justjabka.project_sanguine.registries.ProjectSanguineAttachments;
import justjabka.project_sanguine.registries.ProjectSanguineAttributes;
import justjabka.project_sanguine.registries.ProjectSanguineComponents;
import justjabka.project_sanguine.registries.ProjectSanguineEnvironmentAttributes;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Util;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ProjectSanguineServerTickEvent {
    private static final double SANITY_AURA_AFFECTION_RADIUS = 10;
    private static final int SANITY_AURA_ENTITY_LIMIT = 3;
    private static final float SKY_AURA = 0.05f;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(ProjectSanguineServerTickEvent::handleSanityAura);
    }

    private static void handleSanityAura(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.tickCount % 20 != 0) continue;
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player)) continue;

            ServerLevel level = player.level();

            // Calc aura
            float aura = 0f;
            aura += getSanityFromSanityProvider(player);
            aura += getSanityFromEnvironment(player, level);
            aura += getSanityFromEntities(player, level);

            // Update sanity
            PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
            player.setAttached(
                    ProjectSanguineAttachments.PLAYER_DATA,
                    data.addSanity(player, aura)
            );
        }
    }

    private static float getSanityFromEnvironment(ServerPlayer player, Level level) {
        float envSanityAura = 0f;

        BlockPos blockPos = player.blockPosition();

        boolean canSeeSky = level.canSeeSky(blockPos);
        if (canSeeSky) envSanityAura += SKY_AURA;

        EnvironmentAttributeMap envAttributes = level.getBiome(blockPos).value().getAttributes();

        envSanityAura += envAttributes.applyModifier(
                ProjectSanguineEnvironmentAttributes.SANITY_AURA,
                0.0f
        );

        return envSanityAura;
    }

    private static float getSanityFromEntities(ServerPlayer player, Level level) {
        float entitiesAura = 0f;

        AABB bb = player.getBoundingBox().inflate(SANITY_AURA_AFFECTION_RADIUS);

        // Find all entities that have sanity aura
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, bb, entity -> {
            if (entity == player) return false;
            if (!EntitySelector.NO_SPECTATORS.test(entity)) return false;

            AttributeInstance sanityAura = entity.getAttribute(ProjectSanguineAttributes.SANITY_AURA);
            if (sanityAura == null) return false;

            return sanityAura.getValue() != 0;
        });

        if (entities.isEmpty()) return entitiesAura;

        // Get total aura of entities (amount limited per entity type)
        entitiesAura += (float) entities.stream()
                .mapToDouble(e -> e.getAttributeValue(ProjectSanguineAttributes.SANITY_AURA))
                .sorted()
                .limit(SANITY_AURA_ENTITY_LIMIT)
                .sum();

        return entitiesAura;
    }

    // Sanity Provider
    private static float getSanityFromSanityProvider(ServerPlayer player) {
        float sanityProviderAura = 0f;

        if (player.tickCount % 60 != 0) return sanityProviderAura;

        // Get all equipment with sanity provider component
        List<EquipmentSlot> slotsWithSanityProvider = EquipmentSlot.VALUES.stream()
                .filter(slot -> canSanityProviderUsing(player.getItemBySlot(slot), slot))
                .toList();

        if (slotsWithSanityProvider.isEmpty()) return sanityProviderAura;

        // Find item that need to be damaged
        EquipmentSlot slotToDamage = Util.getRandom(slotsWithSanityProvider, player.getRandom());
        ItemStack itemToDamage = player.getItemBySlot(slotToDamage);

        SanityProviderComponent sanityProvider = itemToDamage.get(ProjectSanguineComponents.SANITY_PROVIDER);

        // Provide sanity and damage item
        if (sanityProvider != null) {
            itemToDamage.hurtAndBreak(sanityProvider.itemDamagePerUse(), player, slotToDamage);
            sanityProviderAura += sanityProvider.sanityPerUse();
        }

        return sanityProviderAura;
    }

    private static boolean canSanityProviderUsing(final ItemStack itemStack, final EquipmentSlot slot) {
        if (!itemStack.has(ProjectSanguineComponents.SANITY_PROVIDER)) return false;

        Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
        return equippable != null && slot == equippable.slot() && !itemStack.nextDamageWillBreak();
    }
}
