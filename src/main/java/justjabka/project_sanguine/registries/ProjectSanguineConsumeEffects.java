package justjabka.project_sanguine.registries;

import com.mojang.serialization.MapCodec;
import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.contents.item.consume_effects.ApplySanityConsumeEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class ProjectSanguineConsumeEffects {
    public static final ConsumeEffect.Type<ApplySanityConsumeEffect> APPLY_SANITY = register(
            "apply_sanity", ApplySanityConsumeEffect.CODEC, ApplySanityConsumeEffect.STREAM_CODEC
    );

    public static void initialize() {
        ProjectSanguine.LOGGER.info("Initializing Consume Effects");
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
