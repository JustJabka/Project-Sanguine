package justjabka.project_sanguine.mixins;

import justjabka.project_sanguine.registries.ProjectSanguineAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Monster.class)
public class MonsterMixin {

    @Inject(method = "createMonsterAttributes", at = @At("RETURN"))
    private static void createMonsterAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue()
                .add(ProjectSanguineAttributes.SANITY_AURA, -0.1);
    }
}
