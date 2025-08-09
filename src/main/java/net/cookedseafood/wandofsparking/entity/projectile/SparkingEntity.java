package net.cookedseafood.wandofsparking.entity.projectile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.cookedseafood.wandofsparking.data.WandOfSparkingConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class SparkingEntity {
    private static final List<SparkingEntity> SPARKING_ENTITIES = new ArrayList<>();
    private static Iterator<SparkingEntity> iterator;
    private ServerWorld world;
    private Vec3d pos;
    private Vec3d velocity;
    private short fuse;
    @Nullable
    private LivingEntity owner;
    private boolean leftOwner;
    private byte hitCount;
    public final Random random = Random.create();

    public SparkingEntity(ServerWorld world) {
        this.world = world;
        this.pos = Vec3d.ZERO;
        this.velocity = Vec3d.ZERO;
        SPARKING_ENTITIES.add(this);
    }

    public static void tickAll() {
        iterator = SPARKING_ENTITIES.iterator();
        while (iterator.hasNext()) {
            iterator.next().tick();
        }
    }

    public void tick() {
        this.velocity.multiply(0.99);
        this.velocity.add(0.0, -0.05, 0.0);
        this.pos = this.pos.add(this.velocity);

        BlockPos blockPos = BlockPos.ofFloored(pos);
        if (!this.world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty() || !this.world.getFluidState(blockPos).isEmpty()) {
            this.remove();
            return;
        }

        if (WandOfSparkingConfig.isParticleVisible) {
            this.spawnTrailParticle();
        }

        List<Entity> targets = world.getOtherEntities(
            null,
            new Box(
                this.pos.x - 0.25,
                this.pos.y - 0.25,
                this.pos.z - 0.25,
                this.pos.x + 0.25,
                this.pos.y + 0.25,
                this.pos.z + 0.25
            )
        );
        if (!this.leftOwner) {
            if (targets.contains(owner)) {
                return;
            } else {
                this.leftOwner = true;
            }
        }

        for (Entity target : targets) {
            this.onHit(target);
            this.fuse /= 2;
            if (++this.hitCount > WandOfSparkingConfig.PIERCE_COUNT) {
                this.remove();
                return;
            }
        }

        if (--this.fuse < 0) {
            this.remove();
        }
    }

    public void onHit(Entity target) {
        target.damage(world, world.getDamageSources().create(DamageTypes.MAGIC, owner), owner.getCastingDamageAgainst(target, WandOfSparkingConfig.CASTING_DAMAGE));
        if (this.random.nextBoolean()) {
            target.setOnFireForTicks(this.random.nextInt(6) == 0 ? this.random.nextBetween(40, 80) : this.random.nextBetween(20, 40));
        }
    }

    public void remove() {
        iterator.remove();
    }

    public void spawnTrailParticle() {
        byte count = (byte)this.random.nextBetween(16, 32);
        for (byte i = 0; i < count; ++i) {
            double delta = Math.abs(this.random.nextDouble() - this.random.nextDouble());
            Vec3d deltaOffset = this.velocity.negate().multiply(delta);

            Vec3d direction = this.velocity.normalize();
            Vec3d cross = direction.crossProduct(new Vec3d(0, 1, 0)).normalize();
            Vec3d crossOffset = cross.multiply(delta * this.random.nextDouble() * (this.random.nextBoolean() ? 1 : -1) * 0.5);

            Vec3d perpendicular = direction.crossProduct(cross).normalize();
            double waveHeight = Math.sin(8 * Math.PI * delta) * 0.125;
            Vec3d waveOffset = perpendicular.multiply(waveHeight);

            double randomOffsetScale = 0.01;
            Vec3d randomOffset = new Vec3d(
                this.random.nextDouble() * (this.random.nextBoolean() ? 1 : -1) * randomOffsetScale,
                this.random.nextDouble() * (this.random.nextBoolean() ? 1 : -1) * randomOffsetScale,
                this.random.nextDouble() * (this.random.nextBoolean() ? 1 : -1) * randomOffsetScale
            );

            Vec3d offset = deltaOffset.add(crossOffset).add(waveOffset).add(randomOffset);
            this.world.spawnParticles(
                ParticleTypes.SMALL_FLAME,
                this.pos.x + offset.x,
                this.pos.y + offset.y,
                this.pos.z + offset.z,
                0,
                offset.x,
                offset.y,
                offset.z,
                32768
            );
        }
    }

    public ServerWorld getWorld() {
        return this.world;
    }

    public void setWorld(ServerWorld world) {
        this.world = world;
    }

    public SparkingEntity withWorld(ServerWorld world) {
        this.setWorld(world);
        return this;
    }

    public Vec3d getPos() {
        return this.pos;
    }

    public void setPos(Vec3d pos) {
        this.pos = pos;
    }

    public SparkingEntity withPos(Vec3d pos) {
        this.setPos(pos);
        return this;
    }

    public Vec3d getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vec3d velocity) {
        this.velocity = velocity;
    }

    public SparkingEntity withVelocity(Vec3d velocity) {
        this.setVelocity(velocity);
        return this;
    }

    public short getFuse() {
        return this.fuse;
    }

    public void setFuse(short fuse) {
        this.fuse = fuse;
    }

    public SparkingEntity withFuse(short fuse) {
        this.setFuse(fuse);
        return this;
    }

    public LivingEntity getOwner() {
        return this.owner;
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    public SparkingEntity withOwner(LivingEntity owner) {
        this.setOwner(owner);
        return this;
    }

    public boolean hasLeftOwner() {
        return this.leftOwner;
    }

    public void setLeftOwner(boolean leftOwner) {
        this.leftOwner = leftOwner;
    }

    public SparkingEntity withLeftOwner(boolean leftOwner) {
        this.setLeftOwner(leftOwner);
        return this;
    }

    public byte getHitCount() {
        return this.hitCount;
    }

    public void setHitCount(byte hitCount) {
        this.hitCount = hitCount;
    }

    public SparkingEntity withHitCount(byte hitCount) {
        this.setHitCount(hitCount);
        return this;
    }
}
