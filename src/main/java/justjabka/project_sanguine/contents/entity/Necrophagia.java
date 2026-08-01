package justjabka.project_sanguine.contents.entity;

import justjabka.project_sanguine.contents.entity.ai.NearestAttackableInsaneTargetGoal;
import justjabka.project_sanguine.contents.entity.projectile.PhantomCharge;
import justjabka.project_sanguine.registries.ProjectSanguineEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Necrophagia extends Monster implements RangedAttackMob {
    private static final double RANGED_ATTACK_REQUIRED_RADIUS = 4 * 4;

    public Necrophagia(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public Necrophagia(Level level) {
        super(ProjectSanguineEntityTypes.NECROPHAGIA, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new NecrophagiaMeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new NecrophagiaRangedAttackGoal(this, 1, 40, 15));
        this.targetSelector.addGoal(2, new NearestAttackableInsaneTargetGoal(this, true, true));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        double yProgress = 0.3;

        double xd = target.getX() - this.getX();
        double yd = target.getY(1.0 / 3.0) - this.getY(yProgress);
        double zd = target.getZ() - this.getZ();

        PhantomCharge charge = new PhantomCharge(serverLevel, this);
        charge.snapTo(this.getX(), this.getY(yProgress), this.getZ());

        charge.shoot(xd, yd, zd, 1.5f, 1.0f);
        serverLevel.addFreshEntity(charge);

        // TODO: add custom sound
        this.playSound(SoundEvents.PHANTOM_BITE, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public static AttributeSupplier.Builder createNecrophagiaAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.FOLLOW_RANGE, 35)
                .add(Attributes.MOVEMENT_SPEED, 0.33)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.ARMOR, 2);
    }

    @Override
    public void tick() {
        super.tick();

        Level level = this.level();

        if (!level.isClientSide()) return;
        if (this.getDeltaMovement().horizontalDistanceSqr() < 0.005D) return;

        double legY = this.getY() + 0.15D;

        double spreadX = (this.random.nextDouble() - 0.5D) * 0.2D;
        double spreadY = (this.random.nextDouble() - 0.5D) * 0.2D;

        level.addParticle(ParticleTypes.MYCELIUM,
                this.getX() + spreadX, legY, this.getZ() + spreadY,
                0.0, 0.01, 0.0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(final DamageSource source) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_STEP;
    }

    @Override
    protected void playStepSound(final BlockPos pos, final BlockState blockState) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    private boolean isTargetFarAway(LivingEntity target) {
        if (target == null) return false;
        double distance = this.distanceToSqr(target);

        return distance >= RANGED_ATTACK_REQUIRED_RADIUS;
    }

    private class NecrophagiaRangedAttackGoal extends RangedAttackGoal {
        public NecrophagiaRangedAttackGoal(RangedAttackMob mob, double speedModifier, int attackInterval, float attackRadius) {
            super(mob, speedModifier, attackInterval, attackRadius);
        }

        @Override
        public boolean canUse() {
            return isTargetFarAway(getTarget()) && super.canUse();
        }
    }

    private class NecrophagiaMeleeAttackGoal extends MeleeAttackGoal {
        public NecrophagiaMeleeAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canUse() {
            return !isTargetFarAway(getTarget()) && super.canUse();
        }
    }
}