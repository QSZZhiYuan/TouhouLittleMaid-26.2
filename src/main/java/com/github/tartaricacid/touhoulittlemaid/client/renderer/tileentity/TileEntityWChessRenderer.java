package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.game.chess.Position;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGomoku;
import com.github.tartaricacid.touhoulittlemaid.client.model.WChessPiecesModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityWChess;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import com.github.tartaricacid.touhoulittlemaid.util.WChessUtil;
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
import net.minecraft.world.phys.AABB;

public class TileEntityWChessRenderer implements BlockEntityRenderer<TileEntityWChess, MaidBlockEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/block/wchess.png");
    private static final Identifier PIECES_TEXTURE = Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/bedrock/block/wchess_pieces.png");
    private static final int TIPS_RENDER_DISTANCE = 16;
    private static final int PIECE_RENDER_DISTANCE = 24;
    private final Font font;
    private SimpleBedrockModel<EntityRenderState> chessModel;
    private final WChessPiecesModel[] chessPiecesModels;
    private final WChessPiecesModel selectedModels;

    public TileEntityWChessRenderer(BlockEntityRendererProvider.Context context) {
        chessPiecesModels = WChessPiecesModel.initModel();
        selectedModels = WChessPiecesModel.getSelectedModel();
        font = Minecraft.getInstance().font;
    }

    @Override
    public MaidBlockEntityRenderState createRenderState() {
        return new MaidBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(TileEntityWChess blockEntity, MaidBlockEntityRenderState state, float partialTicks, net.minecraft.world.phys.Vec3 cameraPosition, net.minecraft.client.renderer.feature.ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.blockEntity = blockEntity;
        state.partialTick = partialTicks;
    }

    @Override
    public void submit(MaidBlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector bufferIn, CameraRenderState camera) {
        TileEntityWChess cchess = (TileEntityWChess) state.blockEntity;
        int combinedLightIn = state.lightCoords;
        int combinedOverlayIn = OverlayTexture.NO_OVERLAY;
        Direction facing = cchess.getBlockState().getValue(BlockGomoku.FACING);
        this.renderChessboard(poseStack, bufferIn, combinedLightIn, combinedOverlayIn, facing);
        this.renderPiece(cchess, poseStack, bufferIn, combinedLightIn, combinedOverlayIn, facing, camera);
        this.renderTipsText(cchess, poseStack, bufferIn, combinedLightIn, camera);
    }

    private void renderTipsText(TileEntityWChess chess, PoseStack poseStack, SubmitNodeCollector bufferIn, int combinedLightIn, CameraRenderState camera) {
        boolean showTips = chess.isCheckmate() || chess.isRepeat() || chess.isMoveNumberLimit();
        if (!showTips || !inRenderDistance(chess, TIPS_RENDER_DISTANCE, camera)) {
            return;
        }

        MutableComponent loseTips = null;
        MutableComponent resetTips = Component.translatable("message.touhou_little_maid.wchess.reset").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.AQUA);
        MutableComponent roundText = Component.translatable("message.touhou_little_maid.gomoku.round", chess.getChessCounter()).withStyle(ChatFormatting.WHITE);
        MutableComponent preRoundIcon = Component.literal("⏹ ").withStyle(ChatFormatting.GREEN);
        MutableComponent postRoundIcon = Component.literal(" ⏹").withStyle(ChatFormatting.GREEN);
        MutableComponent roundTips = preRoundIcon.append(roundText).append(postRoundIcon);

        if (chess.isCheckmate()) {
            if (!chess.isPlayerTurn()) {
                loseTips = Component.translatable("message.touhou_little_maid.gomoku.win").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
            } else {
                loseTips = Component.translatable("message.touhou_little_maid.gomoku.lose").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
            }
        } else if (chess.isMoveNumberLimit()) {
            loseTips = Component.translatable("message.touhou_little_maid.cchess.move_limit").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
        } else if (chess.isRepeat()) {
            loseTips = Component.translatable("message.touhou_little_maid.cchess.repeat").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_PURPLE);
        }
        if (loseTips == null) {
            return;
        }

        float loseTipsWidth = (float) (-this.font.width(loseTips) / 2);
        float resetTipsWidth = (float) (-this.font.width(resetTips) / 2);
        float roundTipsWidth = (float) (-this.font.width(roundTips) / 2);
        poseStack.pushPose();
        poseStack.translate(0.5, 0.75, 0.5);
        bufferIn.submitNameTag(poseStack, new net.minecraft.world.phys.Vec3(0, 0, 0), -10, loseTips, true, combinedLightIn, camera);
        poseStack.popPose();
    }

    private void renderPiece(TileEntityWChess cchess, PoseStack poseStack, SubmitNodeCollector bufferIn, int combinedLightIn, int combinedOverlayIn, Direction facing, CameraRenderState camera) {
        if (inRenderDistance(cchess, PIECE_RENDER_DISTANCE, camera)) {
            int selectX = Position.FILE_X(cchess.getSelectChessPoint());
            int selectY = Position.RANK_Y(cchess.getSelectChessPoint());
            byte[] data = cchess.getChessData().squares;
            poseStack.pushPose();
            switch (facing) {
                case NORTH:
                    poseStack.translate(0.875 + 0.5, 1.625, 0.875 + 0.5);
                    break;
                case EAST:
                    poseStack.translate(-0.875 + 0.5, 1.625, 0.875 + 0.5);
                    break;
                case WEST:
                    poseStack.translate(0.875 + 0.5, 1.625, -0.875 + 0.5);
                    break;
                default:
                    poseStack.translate(-0.875 + 0.5, 1.625, -0.875 + 0.5);
                    break;
            }
            poseStack.mulPose(Axis.ZN.rotationDegrees(180));
            poseStack.mulPose(Axis.YN.rotationDegrees(facing.get2DDataValue() * 90));
            if (facing == Direction.SOUTH || facing == Direction.NORTH) {
                poseStack.mulPose(Axis.YN.rotationDegrees(180));
            }
            bufferIn.submitCustomGeometry(poseStack, RenderTypes.entityCutout(PIECES_TEXTURE), (pose, piecesBuff) -> {
                PoseStack ps = new PoseStack();
                ps.last().set(pose);
                for (int y = Position.RANK_TOP; y <= Position.RANK_BOTTOM; y++) {
                    for (int x = Position.FILE_LEFT; x <= Position.FILE_RIGHT; x++) {
                        byte piecesIndex = data[Position.COORD_XY(x, y)];
                        if (WChessUtil.isWhite(piecesIndex) || WChessUtil.isBlack(piecesIndex)) {
                            WChessPiecesModel chessPiecesModel = this.chessPiecesModels[piecesIndex];
                            chessPiecesModel.renderToBuffer(ps, piecesBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                            if (selectX == x && selectY == y) {
                                selectedModels.renderToBuffer(ps, piecesBuff, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                            }
                        }
                        ps.translate(0.25f, 0, 0);
                    }
                    ps.translate(-0.25f * 8, 0, -0.25f);
                }
            });
            poseStack.popPose();
        }
    }

    private void renderChessboard(PoseStack poseStack, SubmitNodeCollector bufferIn, int combinedLightIn, int combinedOverlayIn, Direction facing) {
        if (this.chessModel == null) {
            this.chessModel = BedrockModelLoader.getModel(BedrockModelLoader.WCHESS);
        }
        if (this.chessModel == null) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(facing.get2DDataValue() * 90));
        if (facing == Direction.SOUTH || facing == Direction.NORTH) {
            poseStack.mulPose(Axis.YN.rotationDegrees(180));
        }
        RenderHelper.renderModelToBuffer(chessModel, poseStack, bufferIn, RenderTypes.entityCutout(TEXTURE), combinedLightIn, combinedOverlayIn, -1);
        poseStack.popPose();
    }

    private boolean inRenderDistance(TileEntityWChess chess, int distance, CameraRenderState camera) {
        BlockPos pos = chess.getBlockPos();
        return camera.pos.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < distance * distance;
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(TileEntityWChess blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return RenderHelper.getAABB(pos.offset(-3, 0, -3), pos.offset(3, 1, 3));
    }
}
