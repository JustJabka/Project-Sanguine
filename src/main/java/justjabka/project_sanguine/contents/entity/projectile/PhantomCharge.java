package justjabka.project_sanguine.contents.entity.projectile;

import justjabka.project_sanguine.registries.ProjectSanguineEntityTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PhantomCharge extends AbstractHurtingProjectile {
    private static final float INERTIA = 0.99f;
    private static final double ACCELERATION_POWER = 0;
    private static final float DAMAGE = 3.0f;
    private static final short MAX_LIFE = 200;

    private int life = 0;

    public PhantomCharge(EntityType<? extends PhantomCharge> type, Level level) {
        super(type, level);
    }

    public PhantomCharge(Level level) {
        super(ProjectSanguineEntityTypes.PHANTOM_CHARGE, level);
        this.accelerationPower = ACCELERATION_POWER;
    }

    public PhantomCharge(Level level, LivingEntity owner) {
        super(ProjectSanguineEntityTypes.PHANTOM_CHARGE, level);
        this.setOwner(owner);
        this.accelerationPower = ACCELERATION_POWER;
    }

    public PhantomCharge(Level level, LivingEntity owner, Vec3 direction) {
        super(ProjectSanguineEntityTypes.PHANTOM_CHARGE, owner, direction, level);
        this.accelerationPower = ACCELERATION_POWER;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) return;

        this.life++;
        if (this.life >= MAX_LIFE) {
            this.discard();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putShort("life", (short) this.life);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.life = input.getShortOr("life", (short) 0);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        LivingEntity owner = this.getOwner() instanceof LivingEntity entity ? entity : null;
        Entity entity = hitResult.getEntity();

        if (owner != null) owner.setLastHurtMob(entity);

        if (!(entity instanceof LivingEntity livingEntity)) return;
        DamageSource source = this.damageSources().indirectMagic(this, owner);

        livingEntity.hurtServer(serverLevel, source, DAMAGE);

        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        this.discard();
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.MYCELIUM;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected float getInertia() {
        return INERTIA;
    }

    @Override
    protected float getLiquidInertia() {
        return INERTIA;
    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity deflectingEntity, @Nullable EntityReference<Entity> newOwner, boolean byAttack) {
        return false;
    }

    @Override
    protected void onDeflection(boolean byAttack) {}
}
