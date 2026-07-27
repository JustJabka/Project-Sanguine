package justjabka.project_sanguine.types;

import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.Range;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;

public enum Sanity implements StringRepresentable {
    NORMAL("normal", Range.of(100f, Float.MAX_VALUE)),
    PARANOIA("paranoia", Range.of(75f, 100f)),
    FEAR("fear", Range.of(50f, 75f)),
    INSANITY("insanity", Range.of(25f, 50f)),
    DELIRIUM("delirium", Range.of(0f, 25f));

    private final String name;
    private final Range<Float> range;

    Sanity(String name, Range<Float> range) {
        this.name = name;
        this.range = range;
    }

    public static Sanity getSanityFromValue(float value) {
        return Arrays.stream(Sanity.values())
                .filter(sanity -> sanity.range.contains(value))
                .findFirst()
                .orElse(Sanity.NORMAL);
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }
}
