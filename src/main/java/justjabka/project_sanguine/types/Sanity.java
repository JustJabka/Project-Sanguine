package justjabka.project_sanguine.types;

import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

import java.util.EnumSet;

public enum Sanity implements StringRepresentable {
    NORMAL("normal", 100f),
    PARANOIA("paranoia", 75f),
    FEAR("fear", 50f),
    INSANITY("insanity", 25f),
    DELIRIUM("delirium", 0f);

    private static final Sanity[] VALUES = Sanity.values();
    private static final EnumSet<Sanity> DANGEROUS_STAGES = EnumSet.of(INSANITY, DELIRIUM);

    private final String name;
    private final float minThreshold;

    Sanity(String name, float minThreshold) {
        this.name = name;
        this.minThreshold = minThreshold;
    }

    public static Sanity getSanityFromValue(float value) {
        for (Sanity stage : VALUES) {
            if (value < stage.minThreshold) continue;
            return stage;
        }

        return DELIRIUM;
    }

    public boolean isAggressivePhantomStage() {
        return DANGEROUS_STAGES.contains(this);
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    public float getMinThreshold() {
        return this.minThreshold;
    }
}
