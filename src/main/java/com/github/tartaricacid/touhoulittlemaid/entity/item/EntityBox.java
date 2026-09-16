package com.github.tartaricacid.touhoulittlemaid.entity.item;

import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class EntityBox extends Entity {
    public static final int FIRST_STAGE = 0;
    public static final int SECOND_STAGE = 1;
    public static final int THIRD_STAGE = 2;
    public static final int MAX_TEXTURE_SIZE = 8;
    private static final EntityDataAccessor<Integer> OPEN_STAGE = SynchedEntityData.defineId(EntityBox.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TEXTURE_INDEX = SynchedEntityData.defineId(EntityBox.class, EntityDataSerializers.INT);
    public static final EntityType<EntityBox> TYPE = EntityType.Builder.<EntityBox>of(EntityBox::new, MobCategory.MISC)
            .sized(2.0f, 2.0f).clientTrackingRange(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("touhou_little_maid", "box")));
    private static final String STAGE_TAG = "OpenStage";
    private static final String TEXTURE_TAG = "TextureIndex";

    // 鐢ㄤ簬绗簩闃舵铔嬬硶鐩掑姩鐢昏绠楃殑鍙橀噺
    public long thirdStageTimeStamp = 0L;
    private int thirdStageTicks = 0;

    public EntityBox(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.setRandomTexture();
    }

    public EntityBox(Level worldIn) {
        this(TYPE, worldIn);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    public void baseTick() {
        if (!this.isNoGravity() && !this.onGround()) {
            this.move(MoverType.SELF, this.getDeltaMovement().add(0, -0.1, 0));
        }
        super.baseTick();

        int stage = this.getOpenStage();
        if (stage == FIRST_STAGE) {
            this.getPassengers().stream().filter(e -> e instanceof TamableAnimal)
                    .forEach(e -> applyInvisibilityEffect((TamableAnimal) e));
        } else if (stage == THIRD_STAGE) {
            thirdStageTicks++;
            if (thirdStageTicks > 100) {
                this.addStageChange();
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        this.addStageChange();
        if (this.getOpenStage() == SECOND_STAGE) {
            this.playSound(InitSounds.BOX_OPEN.get(), 3.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        if (this.getOpenStage() == THIRD_STAGE) {
            this.thirdStageTimeStamp = System.currentTimeMillis();
            this.playSound(InitSounds.BOX_OPEN.get(), 3.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        this.playSound(InitSounds.BOX_OPEN.get(), 3.0f, 2.0f);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OPEN_STAGE, FIRST_STAGE);
        builder.define(TEXTURE_INDEX, 0);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        setOpenStage(input.getIntOr(STAGE_TAG, 0));
        setTextureIndex(input.getIntOr(TEXTURE_TAG, 0));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt(STAGE_TAG, getOpenStage());
        output.putInt(TEXTURE_TAG, getTextureIndex());
    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity other) {
        return this.isAlive();
    }

    @Override
    public boolean isPickable() {
        return this.isAlive();
    }

    public int getOpenStage() {
        return this.entityData.get(OPEN_STAGE);
    }

    private void setOpenStage(int stage) {
        this.entityData.set(OPEN_STAGE, stage);
    }

    public int getTextureIndex() {
        return this.entityData.get(TEXTURE_INDEX);
    }

    private void setTextureIndex(int index) {
        this.entityData.set(TEXTURE_INDEX, Mth.clamp(index, 1, MAX_TEXTURE_SIZE - 1));
    }

    public void setRandomTexture() {
        setTextureIndex(random.nextInt(MAX_TEXTURE_SIZE));
    }

    private void addStageChange() {
        this.setOpenStage(this.getOpenStage() + 1);
        if (this.getOpenStage() > THIRD_STAGE) {
            if (this.level instanceof ServerLevel serverLevel) {
                this.kill(serverLevel);
            } else {
                this.discard();
            }
        }
    }

    @Override
    public void kill(ServerLevel serverLevel) {
        serverLevel.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY() + 0.25, this.getZ(),
                20, 1, 1, 1, 0.2);
        this.getType().getDefaultLootTable().ifPresent(table -> {
            LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(table);
            LootParams.Builder builder = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .withParameter(LootContextParams.ORIGIN, this.position())
                    .withParameter(LootContextParams.DAMAGE_SOURCE, damageSources().genericKill());
            LootParams params = builder.create(LootContextParamSets.ENTITY);
            lootTable.getRandomItems(params, 0, stack -> this.spawnAtLocation(serverLevel, stack));
        });
        super.kill(serverLevel);
    }

    private void applyInvisibilityEffect(TamableAnimal tameable) {
        tameable.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2, 1, false, false));
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity pEntity) {
        return this.position().add(this.getPassengerAttachmentPoint(pEntity, this.getDimensions(Pose.STANDING), 1.0F)).subtract(0, 2, 0);
    }
}
