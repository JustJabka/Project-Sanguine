package justjabka.project_sanguine.registries;

import justjabka.project_sanguine.ProjectSanguine;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;

import java.util.HashMap;
import java.util.Map;

public class SanityRewards {
    private static final Map<EntityType<?>, Float> DEFAULT_REWARDS = new HashMap<>();

    private static final float PHANTOM_SANITY_REWARD = 5f;
    private static final float FRIENDLY_CREATURE_SANITY_REWARD = -2f;
    private static final float NPC_SANITY_REWARD = -2f;

    private static void registerDefaults() {
        DEFAULT_REWARDS.put(EntityTypes.PHANTOM, PHANTOM_SANITY_REWARD);

        BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(type -> {
                    MobCategory category = type.getCategory();
                    if (category == MobCategory.MISC) return false;
                    return category.isFriendly();
                })
                .forEach(type -> DEFAULT_REWARDS.put(type, FRIENDLY_CREATURE_SANITY_REWARD));


        DEFAULT_REWARDS.put(EntityTypes.VILLAGER, NPC_SANITY_REWARD);
        DEFAULT_REWARDS.put(EntityTypes.WANDERING_TRADER, NPC_SANITY_REWARD);
    }

    public static void initialize() {
        ProjectSanguine.LOGGER.info("Initializing Sanity Rewards");
        registerDefaults();
    }

    public static float getDefaultReward(EntityType<?> type) {
        return DEFAULT_REWARDS.getOrDefault(type, 0f);
    }
}
