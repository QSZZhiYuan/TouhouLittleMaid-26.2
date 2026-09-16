package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.network.ClientPacketHelper;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public record OpenBeaconGuiPackage(BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenBeaconGuiPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("open_beacon_gui"));
    public static final StreamCodec<ByteBuf, OpenBeaconGuiPackage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenBeaconGuiPackage::pos,
            OpenBeaconGuiPackage::new
    );

    public static void handle(OpenBeaconGuiPackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> ClientPacketHelper.handleClient("com.github.tartaricacid.touhoulittlemaid.client.network.ClientPacketHandlers", "handleOpenBeaconGui", message));
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
