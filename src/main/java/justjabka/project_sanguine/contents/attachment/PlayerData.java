package justjabka.project_sanguine.contents.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import justjabka.project_sanguine.registries.ProjectSanguineAttributes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

public record PlayerData(float sanity) {
    private static final float DEFAULT_MAX_SANITY = (float) ProjectSanguineAttributes.MAX_SANITY.value().getDefaultValue();
    public static final PlayerData DEFAULT = new PlayerData(DEFAULT_MAX_SANITY);

    public static final Codec<PlayerData> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                Codec.FLOAT.fieldOf("sanity").forGetter(PlayerData::sanity)
        ).apply(instance, PlayerData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerData> STREAM = StreamCodec.composite(
            ByteBufCodecs.FLOAT, PlayerData::sanity,
            PlayerData::new
    );

    // Sanity
    public PlayerData addSanity(Player player, float value) {
        return setSanity(player, this.sanity + value);
    }

    public PlayerData removeSanity(Player player, float value) {
        return setSanity(player, this.sanity - value);
    }

    public PlayerData setSanity(Player player, float value) {
        AttributeInstance maxSanityInstance = player.getAttribute(ProjectSanguineAttributes.MAX_SANITY);
        if (maxSanityInstance == null) throw new NullPointerException();

        float maxSanity = (float) maxSanityInstance.getValue();

        float newSanity = Mth.clamp(value, 0f, maxSanity);
        return new PlayerData(newSanity);
    }
}