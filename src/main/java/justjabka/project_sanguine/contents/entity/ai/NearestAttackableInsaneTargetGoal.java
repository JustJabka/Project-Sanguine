package justjabka.project_sanguine.contents.entity.ai;

import justjabka.project_sanguine.contents.attachment.PlayerData;
import justjabka.project_sanguine.registries.ProjectSanguineAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class NearestAttackableInsaneTargetGoal extends TargetGoal {
    protected final int randomInterval;
    protected final TargetingConditions targetConditions;

    public NearestAttackableInsaneTargetGoal(Mob mob, boolean mustSee, boolean mustReach) {
        this(mob, 10, mustSee, mustReach, null);
    }

    public NearestAttackableInsaneTargetGoal(
            final Mob mob,
            final int randomInterval,
            final boolean mustSee,
            final boolean mustReach,
            final TargetingConditions.@Nullable Selector selector
    ) {
        super(mob, mustSee, mustReach);
        this.randomInterval = reducedTickDelay(randomInterval);
        this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(selector);
    }

    @Override
    public boolean canUse() {
        if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
            return false;
        }

        ServerLevel level = getServerLevel(this.mob);

        List<Player> players = level.getNearbyPlayers(this.targetConditions, this.mob, this.mob.getBoundingBox().inflate(this.getFollowDistance()));
        if (players.isEmpty()) return false;

        players.sort(Comparator.comparing(NearestAttackableInsaneTargetGoal::getSanity));
        for (Player player : players) {
            if (!this.mob.canAttack(player)) continue;

            this.mob.setTarget(player);
            return true;
        }

        return false;
    }

    private static float getSanity(Player player) {
        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
        return data.sanity();
    }
}
