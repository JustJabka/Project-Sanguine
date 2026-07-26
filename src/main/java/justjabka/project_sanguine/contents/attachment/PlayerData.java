package justjabka.project_sanguine.contents.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

public record PlayerData(float sanity) {
    // TODO: Move MAX_SANITY to the attribute
    public static final float MAX_SANITY = 100f;
    public static final float MIN_SANITY = -25f;

    public static final PlayerData DEFAULT = new PlayerData(MAX_SANITY);

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
    public PlayerData addSanity(float value) {
        return setSanity(this.sanity + value);
    }

    public PlayerData removeSanity(float value) {
        return setSanity(this.sanity - value);
    }

    public PlayerData setSanity(float value) {
        float newSanity = Mth.clamp(value, MIN_SANITY, MAX_SANITY);
        return new PlayerData(newSanity);
    }
}