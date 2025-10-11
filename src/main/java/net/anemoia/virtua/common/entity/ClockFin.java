package net.anemoia.virtua.common.entity;

import net.anemoia.virtua.core.registry.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.network.PlayMessages;

public class ClockFin extends AbstractSchoolingFish {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(ClockFin.class, EntityDataSerializers.INT);
    private final AnimationState idleAnimationState = new AnimationState();
    private final AnimationState swimAnimationState = new AnimationState();

    public ClockFin(EntityType<? extends ClockFin> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ClockFin(PlayMessages.SpawnEntity message, Level level) {
        this(ModEntityTypes.CLOCK_FIN.get(), level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public boolean isLarge() {
        return this.getVariant() == 0;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag) {
        this.setVariant(this.random.nextInt(2));
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    public AnimationState getIdleAnimationState() {
        return this.idleAnimationState;
    }

    public AnimationState getSwimAnimationState() {
        return this.swimAnimationState;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            boolean isMoving = this.getDeltaMovement().lengthSqr() > 0.001;
            this.swimAnimationState.animateWhen(isMoving, this.tickCount);
            this.idleAnimationState.animateWhen(!isMoving, this.tickCount);
        }
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(Items.COD_BUCKET); // Replace with your custom bucket item when you have one
    }

    public static boolean checkClockFinSpawnRules(EntityType<ClockFin> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return WaterAnimal.checkSurfaceWaterAnimalSpawnRules(entityType, level, spawnType, pos, random);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 3.0D);
    }
}
