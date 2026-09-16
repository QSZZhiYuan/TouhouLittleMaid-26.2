package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.network.ClientPacketHelper;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.Util;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.IntFunction;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public record SpawnParticlePackage(int entityId, Type particleType, int delayTicks) implements CustomPacketPayload {
    public SpawnParticlePackage(int entityId, Type particleType) {
        this(entityId, particleType, 0);
    }

    public static final CustomPacketPayload.Type<SpawnParticlePackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("spawn_particle"));
    public static final StreamCodec<ByteBuf, SpawnParticlePackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SpawnParticlePackage::entityId,
            Type.STREAM_CODEC,
            SpawnParticlePackage::particleType,
            ByteBufCodecs.VAR_INT,
            SpawnParticlePackage::delayTicks,
            SpawnParticlePackage::new
    );

    public static void handle(SpawnParticlePackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            if (message.delayTicks <= 0) {
                context.enqueueWork(() -> ClientPacketHelper.handleClient("com.github.tartaricacid.touhoulittlemaid.client.network.ClientPacketHandlers", "handleSpawnParticle", message));
            } else {
                context.enqueueWork(() -> CompletableFuture.runAsync(() -> ClientPacketHelper.handleClient("com.github.tartaricacid.touhoulittlemaid.client.network.ClientPacketHandlers", "handleSpawnParticleDelay", message), Util.backgroundExecutor()));
            }
        }
    }

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Type {
        /**
         * 粒子类型
         */
        EXPLOSION, BUBBLE, HEART, RANK_UP, HEAL;
        public static final IntFunction<Type> BY_ID =
                ByIdMap.continuous(
                        Type::ordinal,
                        Type.values(),
                        ByIdMap.OutOfBoundsStrategy.ZERO
                );
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.idMapper(Type.BY_ID, Type::ordinal);
    }
}
