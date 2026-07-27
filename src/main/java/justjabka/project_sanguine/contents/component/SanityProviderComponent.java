package justjabka.project_sanguine.contents.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SanityProviderComponent(float sanityPerUse, int itemDamagePerUse) {
    public static final Codec<SanityProviderComponent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.FLOAT.fieldOf("sanity_per_use").forGetter(SanityProviderComponent::sanityPerUse),
                Codec.INT.optionalFieldOf("item_damage_per_use", 1).forGetter(SanityProviderComponent::itemDamagePerUse)
        ).apply(builder, SanityProviderComponent::new);
    });
}
