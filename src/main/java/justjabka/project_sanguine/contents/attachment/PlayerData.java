package justjabka.project_sanguine.contents.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

public record PlayerData(int sanity) {
    // TODO: Move MAX_SANITY to the attribute
    private static final int MAX_SANITY = 100;
    private static final int MIN_SANITY = 0;

    public static final PlayerData DEFAULT = new PlayerData(MAX_SANITY);

    public static final Codec<PlayerData> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                Codec.INT.fieldOf("sanity").forGetter(PlayerData::sanity)
        ).apply(instance, PlayerData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerData> STREAM = StreamCodec.composite(
            ByteBufCodecs.INT, PlayerData::sanity,
            PlayerData::new
    );

    // Sanity
    public PlayerData addSanity(int value) {
        return setSanity(this.sanity + value);
    }

    public PlayerData removeSanity(int value) {
        return setSanity(this.sanity - value);
    }

    public PlayerData setSanity(int value) {
        int newSanity = Mth.clamp(value, MIN_SANITY, MAX_SANITY);
        return new PlayerData(newSanity);
    }
}