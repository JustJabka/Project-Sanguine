package justjabka.project_sanguine.mixins;

import justjabka.project_sanguine.registries.SanityRewards;
import justjabka.project_sanguine.types.SanityRewardHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements SanityRewardHolder {

    @Unique
    private static final String TAG_SANITY_REWARD = "sanity_reward";

    @Unique
    public float sanityReward;

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
        output.putFloat(TAG_SANITY_REWARD, this.sanityReward);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        this.sanityReward = input.getFloatOr(TAG_SANITY_REWARD, SanityRewards.getDefaultReward(self.getType()));
    }

    @Override
    public float project_sanguine$getSanityReward() {
        return this.sanityReward;
    }

    @Override
    public void project_sanguine$setSanityReward(float value) {
        this.sanityReward = value;
    }
}
