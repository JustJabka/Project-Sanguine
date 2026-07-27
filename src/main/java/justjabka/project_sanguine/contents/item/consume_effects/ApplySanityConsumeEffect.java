package justjabka.project_sanguine.contents.item.consume_effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import justjabka.project_sanguine.contents.attachment.PlayerData;
import justjabka.project_sanguine.registries.ProjectSanguineAttachments;
import justjabka.project_sanguine.registries.ProjectSanguineConsumeEffects;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public record ApplySanityConsumeEffect(float amount, float probability) implements ConsumeEffect {
    private static final float DEFAULT_SANITY = 0f;
    private static final float DEFAULT_PROBABILITY = 1f;

    public static final MapCodec<ApplySanityConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance ->
                    instance.group(
                                    Codec.FLOAT.optionalFieldOf("amount", DEFAULT_SANITY).forGetter(ApplySanityConsumeEffect::amount),
                                    Codec.floatRange(0f, 1f).optionalFieldOf("probability", DEFAULT_PROBABILITY).forGetter(ApplySanityConsumeEffect::probability)
                            )
                            .apply(instance, ApplySanityConsumeEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ApplySanityConsumeEffect> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ApplySanityConsumeEffect::amount,
            ByteBufCodecs.FLOAT, ApplySanityConsumeEffect::probability,
            ApplySanityConsumeEffect::new
    );

    public ApplySanityConsumeEffect() {
        this(DEFAULT_SANITY, DEFAULT_PROBABILITY);
    }

    @Override
    public @NonNull Type<? extends ConsumeEffect> getType() {
        return ProjectSanguineConsumeEffects.APPLY_SANITY;
    }

    @Override
    public boolean apply(Level level, ItemStack stack, LivingEntity user) {
        if (!(user instanceof Player player)) return false;

        if (player.getRandom().nextFloat() >= this.probability) return false;

        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
        player.setAttached(
                ProjectSanguineAttachments.PLAYER_DATA,
                data.addSanity(player, amount)
        );

        return true;
    }
}
