package justjabka.project_sanguine.mixins;

import justjabka.project_sanguine.managers.SanityManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

    @Shadow
    private int nextTick;

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void tick(ServerLevel level, boolean spawnEnemies, CallbackInfo ci) {
        if (!spawnEnemies) return;
        if (!level.getGameRules().get(GameRules.SPAWN_PHANTOMS)) return;

        --this.nextTick;
        if (nextTick > 0) return;

        RandomSource random = level.getRandom();
        nextTick = nextTick + (45 + random.nextInt(45)) * 20;

        if (level.getSkyDarken() < 5) return;
        if (!level.dimensionType().hasSkyLight()) return;

        for (ServerPlayer player : level.players()) {
            if (player.isSpectator()) continue;

            if (!SanityManager.isInsane(player)) continue;

            BlockPos playerPos = player.blockPosition();

            if (playerPos.getY() < level.getSeaLevel()) continue;
            if (!level.canSeeSky(playerPos)) continue;

            DifficultyInstance difficulty = level.getCurrentDifficultyAt(playerPos);

            double spawnChance = SanityManager.getPhantomSpawnChance(player);
            if (random.nextDouble() > spawnChance) continue;

            spawnGroup(level, playerPos, difficulty, random);
        }

        ci.cancel();
    }

    @Unique
    private static void spawnGroup(ServerLevel level, BlockPos playerPos, DifficultyInstance difficulty, RandomSource random) {
        BlockPos spawnPos = playerPos.above(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21));
        BlockState blockState = level.getBlockState(spawnPos);
        FluidState fluidState = level.getFluidState(spawnPos);

        if (!NaturalSpawner.isValidEmptySpawnBlock(level, spawnPos, blockState, fluidState, EntityTypes.PHANTOM)) return;

        SpawnGroupData groupData = null;
        int groupSize = 1 + random.nextInt(difficulty.getDifficulty().getId() + 1);

        for (int i = 0; i < groupSize; i++) {
            Phantom phantom = EntityTypes.PHANTOM.create(level, EntitySpawnReason.NATURAL);
            if (phantom == null) continue;

            phantom.snapTo(spawnPos, 0.0F, 0.0F);
            groupData = phantom.finalizeSpawn(level, difficulty, EntitySpawnReason.NATURAL, groupData);
            level.addFreshEntityWithPassengers(phantom);
        }
    }
}
