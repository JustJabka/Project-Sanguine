package justjabka.project_sanguine.type;

import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.Range;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;

public enum Sanity implements StringRepresentable {
    NORMAL("normal", Range.of(76, 100)),
    PARANOIA("paranoia", Range.of(51, 75)),
    FEAR("fear", Range.of(26, 50)),
    INSANITY("insanity", Range.of(0, 25)),
    DELIRIUM("delirium", Range.of(-25, -1));

    private final String name;
    private final Range<Integer> range;

    Sanity(String name, Range<Integer> range) {
        this.name = name;
        this.range = range;
    }

    public static Sanity getSanityFromValue(int value) {
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
