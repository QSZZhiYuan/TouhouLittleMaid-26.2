package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.network.ClientPacketHelper;
import com.github.tartaricacid.touhoulittlemaid.util.WChessUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.concurrent.CompletableFuture;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public record WChessToClientPackage(BlockPos pos, String fenData) implements CustomPacketPayload {
    public static final Type<WChessToClientPackage> TYPE = new Type<>(getResourceLocation("wchess_to_client"));
    public static final StreamCodec<ByteBuf, WChessToClientPackage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            WChessToClientPackage::pos,
            ByteBufCodecs.STRING_UTF8,
            WChessToClientPackage::fenData,
            WChessToClientPackage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(WChessToClientPackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> CompletableFuture.runAsync(() -> ClientPacketHelper.handleClient("com.github.tartaricacid.touhoulittlemaid.client.network.ClientPacketHandlers", "handleWChessToClient", message), Util.backgroundExecutor()));
        }
    }
}
