package justjabka.project_sanguine.events;

import justjabka.project_sanguine.managers.SanityManager;
import justjabka.project_sanguine.types.SanityRewardHolder;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ProjectSanguineEntityDeathEvent {
    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register(ProjectSanguineEntityDeathEvent::handleSanityReward);
    }

    private static void handleSanityReward(LivingEntity entity, DamageSource damageSource) {
        if (!(damageSource.getEntity() instanceof Player player)) return;
        if (!(entity instanceof SanityRewardHolder holder)) return;

        SanityManager.giveSanityRewardAfterKill(player, holder);
    }
}
