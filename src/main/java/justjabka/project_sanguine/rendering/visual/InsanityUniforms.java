package justjabka.project_sanguine.rendering.visual;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.DynamicUniformStorage;

import java.nio.ByteBuffer;

@Environment(EnvType.CLIENT)
public class InsanityUniforms implements AutoCloseable {
    public static final int INSANITY_UBO_SIZE = new Std140SizeCalculator().putFloat().putFloat().get();
    private static final int INITIAL_CAPACITY = 2;
    private final DynamicUniformStorage<Insanity> progress = new DynamicUniformStorage<>("Sanity UBO", INSANITY_UBO_SIZE, INITIAL_CAPACITY);

    public void reset() {
        this.progress.endFrame();
    }

    @Override
    public void close() {
        this.progress.close();
    }

    public GpuBufferSlice writeProgress(final float progress, final float time) {
        return this.progress.writeUniform(new Insanity(progress, time));
    }

    @Environment(EnvType.CLIENT)
    public record Insanity(float progress, float time)
            implements DynamicUniformStorage.DynamicUniform {
        @Override
        public void write(final ByteBuffer buffer) {
            Std140Builder.intoBuffer(buffer).putFloat(this.progress).putFloat(this.time);
        }
    }
}
