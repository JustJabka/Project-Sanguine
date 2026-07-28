package justjabka.project_sanguine.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

    // TODO: Write custom phantom spawn logic. For now vanilla one will be disabled
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(ServerLevel level, boolean spawnEnemies, CallbackInfo ci) {
        ci.cancel();
    }
}
