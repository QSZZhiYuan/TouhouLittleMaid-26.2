package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.network.ClientPacketHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public record CheckSchedulePosPacket(String tips) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CheckSchedulePosPacket> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("check_schedule_pos"));
    public static final StreamCodec<ByteBuf, CheckSchedulePosPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            CheckSchedulePosPacket::tips,
            CheckSchedulePosPacket::new
    );

    public static void handle(CheckSchedulePosPacket message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> ClientPacketHelper.handleClient("com.github.tartaricacid.touhoulittlemaid.client.network.ClientPacketHandlers", "handleCheckSchedulePos", message));
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
