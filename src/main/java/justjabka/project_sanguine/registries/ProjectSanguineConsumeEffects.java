package justjabka.project_sanguine.registries;

import com.mojang.serialization.MapCodec;
import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.contents.item.consume_effects.ApplySanityConsumeEffect;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class ProjectSanguineConsumeEffects {
    public static final ConsumeEffect.Type<ApplySanityConsumeEffect> APPLY_SANITY = register(
            "apply_sanity", ApplySanityConsumeEffect.CODEC, ApplySanityConsumeEffect.STREAM_CODEC
    );

    public static void initialize() {
        ProjectSanguine.LOGGER.info("Initializing Consume Effects");
        modifyVanillaConsumeEffects();
    }

    private static void modifyVanillaConsumeEffects() {
        ProjectSanguine.LOGGER.info("Modifying vanilla's Consume Effects");

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            // Cheap positive instant-sanity
            modifyConsumeEffects(Items.SWEET_BERRIES, 1.0f, 0.5f);
            modifyConsumeEffects(Items.GLOW_BERRIES, 1.0f, 0.5f);
            modifyConsumeEffects(Items.MELON_SLICE, 1.0f, 0.8f);
            modifyConsumeEffects(Items.APPLE, 3.0f, 1.0f);
            modifyConsumeEffects(Items.CHORUS_FRUIT, 1.0f, 0.5f);
            modifyConsumeEffects(Items.GOLDEN_APPLE, 3.0f, 1.0f);
            modifyConsumeEffects(Items.ENCHANTED_GOLDEN_APPLE, 5.0f, 1.0f);

            // Fast positive instant-sanity
            modifyConsumeEffects(Items.COOKIE, 3.0f, 1.0f);
            modifyConsumeEffects(Items.HONEY_BOTTLE, 25.0f, 1.0f);
            modifyConsumeEffects(Items.PUMPKIN_PIE, 5.0f, 1.0f);
            modifyConsumeEffects(Items.CAKE, 12.0f, 1.0f);

            // Cheap negative instant-sanity
            modifyConsumeEffects(Items.COD, -5.0f, 0.4f);
            modifyConsumeEffects(Items.SALMON, -5.0f, 0.4f);
            modifyConsumeEffects(Items.MUTTON, -6.0f, 0.5f);
            modifyConsumeEffects(Items.RABBIT, -6.0f, 0.5f);
            modifyConsumeEffects(Items.BEEF, -7.0f, 0.6f);
            modifyConsumeEffects(Items.PORKCHOP, -7.0f, 0.6f);
            modifyConsumeEffects(Items.CHICKEN, -8.0f, 0.8f);
            modifyConsumeEffects(Items.TROPICAL_FISH, -10.0f, 0.7f);

            // Fast negative instant-sanity
            modifyConsumeEffects(Items.SPIDER_EYE, -30.0f, 1.0f);
            modifyConsumeEffects(Items.ROTTEN_FLESH, -10.0f, 0.5f);
            modifyConsumeEffects(Items.PUFFERFISH, -50.0f, 1.0f);
        });
    }

    private static void modifyConsumeEffects(Item item, float sanityAmount, float probability) {
        Consumable consumable = item.components().get(DataComponents.CONSUMABLE);

        if (consumable == null) return;
        consumable.onConsumeEffects().add(new ApplySanityConsumeEffect(sanityAmount, probability));
    }

    private static <T extends ConsumeEffect> ConsumeEffect.Type<T> register(
            final String name, final MapCodec<T> codec, final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec
    ) {
        return Registry.register(
                BuiltInRegistries.CONSUME_EFFECT_TYPE,
                ProjectSanguine.id(name),
                new ConsumeEffect.Type<>(codec, streamCodec)
        );
    }
}
