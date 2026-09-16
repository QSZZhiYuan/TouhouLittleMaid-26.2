package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Statue;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGomoku;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.phys.AABB;

public class TileEntityGomokuRenderer implements BlockEntityRenderer<TileEntityGomoku, MaidBlockEntityRenderState> {
    private static final Identifier CHECKER_BOARD_TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/block/gomoku.png");
    private static final Identifier BLACK_PIECE_TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/block/gomoku_black_piece.png");
    private static final Identifier WHITE_PIECE_TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/block/gomoku_white_piece.png");
    private static final int TIPS_RENDER_DISTANCE = 16;
    private static final int PIECE_RENDER_DISTANCE = 24;
    private SimpleBedrockModel<EntityRenderState> CHECKER_BOARD_MODEL;
    private SimpleBedrockModel<EntityRenderState> PIECE_MODEL;
    private final Font font;

    public TileEntityGomokuRenderer(BlockEntityRendererProvider.Context context) {
        this.font = Minecraft.getInstance().font;
    }

    @Override
    public MaidBlockEntityRenderState createRenderState() {
        return new MaidBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(TileEntityGomoku blockEntity, MaidBlockEntityRenderState state, float partialTicks, net.minecraft.world.phys.Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.blockEntity = blockEntity;
        state.partialTick = partialTicks;
    }

    @Override
    public void submit(MaidBlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector bufferIn, CameraRenderState camera) {
        TileEntityGomoku gomoku = (TileEntityGomoku) state.blockEntity;
        int combinedLightIn = state.lightCoords;
        int combinedOverlayIn = OverlayTexture.NO_OVERLAY;
        this.renderChessboard(gomoku, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
        this.renderPiece(gomoku, poseStack, bufferIn, combinedLightIn, combinedOverlayIn, camera);
        this.renderLatestChessTips(gomoku, poseStack, bufferIn, combinedLightIn, camera);
        this.renderTipsText(gomoku, poseStack, bufferIn, combinedLightIn, camera);
    }

    private void renderLatestChessTips(TileEntityGomoku gomoku, PoseStack poseStack, SubmitNodeCollector bufferIn, int combinedLightIn, CameraRenderState camera) {
        if (!gomoku.getLatestChessPoint().equals(Point.NULL) && inRenderDistance(gomoku, PIECE_RENDER_DISTANCE, camera)) {
            Point point = gomoku.getLatestChessPoint();
            poseStack.pushPose();
            poseStack.translate(-0.42, 0.25, -0.42);
            poseStack.translate(point.x * 0.1316, 0, point.y * 0.1316);
            poseStack.mulPose(Axis.YN.rotationDegrees(180 + camera.yRot));
            poseStack.scale(0.015625F, -0.015625F, 0.015625F);
            float width = (float) (-this.font.width("▼") / 2) + 0.5f;
            bufferIn.submitText(poseStack, width, -1.5f, FormattedCharSequence.forward("▼", net.minecraft.network.chat.Style.EMPTY), false, Font.DisplayMode.POLYGON_OFFSET, combinedLightIn, 0xFF0000, 0, 0);
            poseStack.popPose();
        }
    }

    private void renderChessboard(TileEntityGomoku gomoku, PoseStack poseStack, SubmitNodeCollector bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (this.CHECKER_BOARD_MODEL == null) {
            this.CHECKER_BOARD_MODEL = BedrockModelLoader.getModel(BedrockModelLoader.GOMOKU);
        }
        if (this.CHECKER_BOARD_MODEL == null) {
            return;
        }
        Direction facing = gomoku.getBlockState().getValue(BlockGomoku.FACING);
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(facing.get2DDataValue() * 90));
        if (facing == Direction.SOUTH || facing == Direction.NORTH) {
            poseStack.mulPose(Axis.YN.rotationDegrees(180));
        }
        RenderHelper.renderModelToBuffer(CHECKER_BOARD_MODEL, poseStack, bufferIn, RenderTypes.entityCutout(CHECKER_BOARD_TEXTURE), combinedLightIn, combinedOverlayIn, -1);
        poseStack.popPose();
    }

    private void renderPiece(TileEntityGomoku gomoku, PoseStack poseStack, SubmitNodeCollector bufferIn, int combinedLightIn, int combinedOverlayIn, CameraRenderState camera) {
        if (this.PIECE_MODEL == null) {
            this.PIECE_MODEL = BedrockModelLoader.getModel(BedrockModelLoader.GOMOKU_PIECE);
        }
        if (this.PIECE_MODEL == null) {
            return;
        }
        if (inRenderDistance(gomoku, PIECE_RENDER_DISTANCE, camera)) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1.5, 0.5);
            poseStack.mulPose(Axis.ZN.rotationDegrees(180));
            poseStack.translate(0.92, -0.1, -1.055);
            byte[][] chessData = gomoku.getChessData();
            for (byte[] row : chessData) {
                for (int j = 0; j < chessData[0].length; j++) {
                    poseStack.translate(0, 0, 0.1316);
                    if (row[j] == Point.BLACK) {
                        RenderHelper.renderModelToBuffer(PIECE_MODEL, poseStack, bufferIn, RenderTypes.entityCutout(BLACK_PIECE_TEXTURE), combinedLightIn, combinedOverlayIn, -1);
                    }
                    if (row[j] == Point.WHITE) {
                        RenderHelper.renderModelToBuffer(PIECE_MODEL, poseStack, bufferIn, RenderTypes.entityCutout(WHITE_PIECE_TEXTURE), combinedLightIn, combinedOverlayIn, -1);
                    }
                }
                poseStack.translate(-0.1316, 0, -1.974);
            }
            poseStack.popPose();
        }
    }

    private void renderTipsText(TileEntityGomoku gomoku, PoseStack poseStack, SubmitNodeCollector bufferIn, int combinedLightIn, CameraRenderState camera) {
        Statue statue = gomoku.getStatue();

        if (statue != Statue.IN_PROGRESS && inRenderDistance(gomoku, TIPS_RENDER_DISTANCE, camera)) {
            MutableComponent loseTips;
            MutableComponent resetTips = Component.translatable("message.touhou_little_maid.gomoku.reset").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.AQUA);
            MutableComponent roundText = Component.translatable("message.touhou_little_maid.gomoku.round", gomoku.getChessCounter()).withStyle(ChatFormatting.WHITE);
            MutableComponent preRoundIcon = Component.literal("⏹ ").withStyle(ChatFormatting.GREEN);
            MutableComponent postRoundIcon = Component.literal(" ⏹").withStyle(ChatFormatting.GREEN);
            MutableComponent roundTips = preRoundIcon.append(roundText).append(postRoundIcon);
            if (statue == Statue.WIN) {
                if (gomoku.isPlayerTurn()) {
                    loseTips = Component.translatable("message.touhou_little_maid.gomoku.win").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
                } else {
                    loseTips = Component.translatable("message.touhou_little_maid.gomoku.lose").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
                }
            } else {
                loseTips = Component.translatable("message.touhou_little_maid.gomoku.draw").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
            }
            float loseTipsWidth = (float) (-this.font.width(loseTips) / 2);
            float resetTipsWidth = (float) (-this.font.width(resetTips) / 2);
            float roundTipsWidth = (float) (-this.font.width(roundTips) / 2);
            poseStack.pushPose();
            poseStack.translate(0.5, 0.75, 0.5);
            bufferIn.submitNameTag(poseStack, new net.minecraft.world.phys.Vec3(0, 0, 0), -10, loseTips, true, combinedLightIn, camera);
            poseStack.popPose();
        }
    }

    private boolean inRenderDistance(TileEntityGomoku gomoku, int distance, CameraRenderState camera) {
        BlockPos pos = gomoku.getBlockPos();
        return camera.pos.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < distance * distance;
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(TileEntityGomoku blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return RenderHelper.getAABB(pos.offset(-3, 0, -3), pos.offset(3, 1, 3));
    }
}
