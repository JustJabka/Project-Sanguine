package justjabka.project_sanguine.managers;

import justjabka.project_sanguine.contents.attachment.PlayerData;
import justjabka.project_sanguine.contents.component.SanityProviderComponent;
import justjabka.project_sanguine.registries.ProjectSanguineAttachments;
import justjabka.project_sanguine.registries.ProjectSanguineAttributes;
import justjabka.project_sanguine.registries.ProjectSanguineComponents;
import justjabka.project_sanguine.registries.ProjectSanguineEnvironmentAttributes;
import justjabka.project_sanguine.types.Sanity;
import justjabka.project_sanguine.types.SanityRewardHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SanityManager {
    private static final double SANITY_AURA_AFFECTION_RADIUS = 10;
    private static final int SANITY_AURA_ENTITY_LIMIT = 3;
    private static final float SKY_AURA = 0.05f;
    private static final float DARKNESS_AURA = -0.2f;
    private static final float DARKNESS_AURA_WEAKENING_FROM_LIGHT_MULTIPLIER = 0.5f;
    private static final float BONUS_SANITY_FROM_SLEEP = 15f;
    private static final float INSOMNIA_AURA_MULTIPLIER = -0.1f;

    public static boolean isInsane(Player player) {
        Sanity sanity = getSanityStage(player);
        return sanity.isAggressivePhantomStage();
    }

    public static Sanity getSanityStage(Player player) {
        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
        return Sanity.getSanityFromValue(data.sanity());
    }

    public static double getPhantomSpawnChance(Player player) {
        Sanity sanity = getSanityStage(player);

        switch (sanity) {
            case INSANITY -> {
                return 0.5;
            }
            case DELIRIUM -> {
                return 0.8;
            }
            default -> {
                return 0;
            }
        }
    }

    /**
     * Gets passive aura
     * @param player Player
     * @return Total passive aura
     */
    public static float getPassiveAura(ServerPlayer player) {
        float passiveAura = 0f;

        ServerLevel level = player.level();

        passiveAura += getInsomniaAura(player);
        passiveAura += getAuraFromEnvironment(player, level);
        passiveAura += getAuraFromEntities(player, level);

        return passiveAura;
    }

    public static void restoreSanityAfterSleep(Player player) {
        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
        player.setAttached(
                ProjectSanguineAttachments.PLAYER_DATA,
                data.addSanity(player, BONUS_SANITY_FROM_SLEEP)
        );
    }

    public static void giveSanityRewardAfterKill(Player player, SanityRewardHolder holder) {
        float sanityReward = holder.project_sanguine$getSanityReward();
        if (sanityReward == 0f) return;

        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
        player.setAttached(
                ProjectSanguineAttachments.PLAYER_DATA,
                data.addSanity(player, sanityReward)
        );
    }

    /**
     * Gets aura from insomnia
     * @param player Player
     * @return Aura from insomnia
     */
    private static float getInsomniaAura(ServerPlayer player) {
        float insomniaAura = 0f;

        ServerStatsCounter stats = player.getStats();
        int timeSinceRest = Mth.clamp(stats.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);

        final int DAY_LENGTH = 24000;
        final int MIN_INSOMNIA_TIME = DAY_LENGTH * 3;

        if (timeSinceRest < MIN_INSOMNIA_TIME) return insomniaAura;

        float daysWithoutSleep = (float) (timeSinceRest - MIN_INSOMNIA_TIME) / DAY_LENGTH + 1; // OHHH NO!!! LE MAGIC NUMBER😭

        return daysWithoutSleep * INSOMNIA_AURA_MULTIPLIER;
    }

    /**
     * Gets aura from environment (sky/darness and biome auras)
     * @param player Player
     * @param level Level
     * @return Aura from environment
     * @see ProjectSanguineEnvironmentAttributes#SANITY_AURA
     */
    private static float getAuraFromEnvironment(ServerPlayer player, Level level) {
        float envSanityAura = 0f;

        BlockPos blockPos = player.blockPosition();

        boolean canSeeSky = level.canSeeSky(blockPos);

        // Sky/Darkness Aura
        if (canSeeSky) envSanityAura += SKY_AURA;
        else envSanityAura += getDarknessAura(level, blockPos);

        // Environment Attribute Aura
        EnvironmentAttributeMap envAttributes = level.getBiome(blockPos).value().getAttributes();
        envSanityAura += envAttributes.applyModifier(
                ProjectSanguineEnvironmentAttributes.SANITY_AURA,
                0.0f
        );

        return envSanityAura;
    }

    private static float getDarknessAura(Level level, BlockPos blockPos) {
        float darknessPenalty = DARKNESS_AURA;

        int skyLight = level.getBrightness(LightLayer.SKY, blockPos);
        int blockLight = level.getBrightness(LightLayer.BLOCK, blockPos);

        if (skyLight >= 8) return 0f;
        if (blockLight >= 8) {
            darknessPenalty *= DARKNESS_AURA_WEAKENING_FROM_LIGHT_MULTIPLIER;
        }

        return darknessPenalty;
    }

    /**
     * Gets aura from entities in radius of {@value SANITY_AURA_AFFECTION_RADIUS}
     * <p>
     * Limited by {@value SANITY_AURA_ENTITY_LIMIT} entities
     * @param level Level
     * @return Aura from entities
     * @see ProjectSanguineAttributes#SANITY_AURA
     */
    private static float getAuraFromEntities(ServerPlayer player, Level level) {
        float entitiesAura = 0f;

        List<LivingEntity> entities = getEntitiesWithAura(player, level);
        if (entities.isEmpty()) return entitiesAura;

        entitiesAura += (float) entities.stream()
                .mapToDouble(e -> e.getAttributeValue(ProjectSanguineAttributes.SANITY_AURA))
                .sorted()
                .limit(SANITY_AURA_ENTITY_LIMIT)
                .sum();

        return entitiesAura;
    }

    private static List<LivingEntity> getEntitiesWithAura(Player player, Level level) {
        AABB bb = player.getBoundingBox().inflate(SANITY_AURA_AFFECTION_RADIUS);

        return level.getEntitiesOfClass(LivingEntity.class, bb, entity -> {
            if (entity == player) return false;
            if (!EntitySelector.NO_SPECTATORS.test(entity)) return false;

            AttributeInstance sanityAura = entity.getAttribute(ProjectSanguineAttributes.SANITY_AURA);
            if (sanityAura == null) return false;

            return sanityAura.getValue() != 0;
        });
    }

    /**
     * Uses sanity provider from random slot
     * @param player Player
     * @return Sanity got from the item
     * @see ProjectSanguineComponents#SANITY_PROVIDER
     */
    public static float useSanityProvider(ServerPlayer player) {
        float sanityProviderAura = 0f;

        EquipmentSlot slot = getRandomSlotWithSanityProvider(player);
        if (slot == null) return sanityProviderAura;

        ItemStack item = player.getItemBySlot(slot);
        SanityProviderComponent sanityProvider = item.get(ProjectSanguineComponents.SANITY_PROVIDER);

        if (sanityProvider != null) {
            item.hurtAndBreak(sanityProvider.itemDamagePerUse(), player, slot);
            sanityProviderAura += sanityProvider.sanityPerUse();
        }

        return sanityProviderAura;
    }

    private static EquipmentSlot getRandomSlotWithSanityProvider(Player player) {
        List<EquipmentSlot> slotsWithSanityProvider = EquipmentSlot.VALUES.stream()
                .filter(slot -> canSanityProviderUsing(player.getItemBySlot(slot), slot))
                .toList();

        return slotsWithSanityProvider.isEmpty() ? null : Util.getRandom(slotsWithSanityProvider, player.getRandom());
    }

    private static boolean canSanityProviderUsing(final ItemStack itemStack, final EquipmentSlot slot) {
        if (!itemStack.has(ProjectSanguineComponents.SANITY_PROVIDER)) return false;

        Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
        return equippable != null && slot == equippable.slot() && !itemStack.nextDamageWillBreak();
    }
}
