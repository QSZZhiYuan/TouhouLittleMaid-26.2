package com.github.tartaricacid.touhoulittlemaid.client.network;

import com.github.tartaricacid.touhoulittlemaid.api.game.chess.Position;
import com.github.tartaricacid.touhoulittlemaid.api.game.chess.Search;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.client.event.MaidAreaRenderEvent;
import com.github.tartaricacid.touhoulittlemaid.client.gui.block.MaidBeaconGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.block.ModelSwitcherGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.cache.CacheIconManager;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.AIChatScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor.LLMSiteEditorScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor.TTSSiteEditorScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsHubScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsLLMSiteScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.other.CheckSchedulePosGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.item.FoxScrollScreen;
import com.github.tartaricacid.touhoulittlemaid.client.sound.data.MaidAISoundInstance;
import com.github.tartaricacid.touhoulittlemaid.client.sound.data.MaidSoundInstance;
import com.github.tartaricacid.touhoulittlemaid.client.sound.data.MaidSoundInstanceAtPos;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.UpdateRemoteStructEvent;
import com.github.tartaricacid.touhoulittlemaid.data.MaidNumAttachment;
import com.github.tartaricacid.touhoulittlemaid.data.PowerAttachment;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidGomokuAI;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.SchedulePos;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.BeaconAbsorbPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.CChessToClientPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.CChessToServerPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.CheckSchedulePosPacket;
import com.github.tartaricacid.touhoulittlemaid.network.message.FoxScrollPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.GomokuClientPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.GomokuServerPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.ItemBreakPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.MaidAnimationPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenBeaconGuiPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenChairGuiPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenPlayerInventoryPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenSwitcherGuiPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.PlayMaidSoundAtPosPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.PlayMaidSoundPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SendEffectPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SpawnParticlePackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncBaublePackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncDataPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncMaidAreaPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncYsmMaidDataPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.WChessToClientPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.WChessToServerPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.ai.SyncAISitesPacket;
import com.github.tartaricacid.touhoulittlemaid.network.message.ai.SyncMaidAIDataPacket;
import com.github.tartaricacid.touhoulittlemaid.network.message.ai.TTSAudioToClientPackage;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityModelSwitcher;
import com.github.tartaricacid.touhoulittlemaid.util.CChessUtil;
import com.github.tartaricacid.touhoulittlemaid.util.WChessUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.NeoForge;

public class ClientPacketHandlers {
    public static void handleBeaconAbsorb(BeaconAbsorbPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            EntityPowerPoint.spawnExplosionParticle(mc.level, message.x(), message.y(), message.z(), mc.level.getRandom());
        }
    }

    public static void handleCChessToClient(CChessToClientPackage message) {
        int levelTime = 1000;
        long timeStart = System.currentTimeMillis();
        int move = 0;

        com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight.Position position = new com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight.Position();
        position.fromFen(message.fenData());

        boolean maidLost = CChessUtil.isMaid(position) && position.isMate();
        boolean playerLost = false;
        if (!maidLost) {
            move = new com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight.Search(position, 12).searchMain(levelTime);
            playerLost = position.makeMove(move) && CChessUtil.isPlayer(position) && position.isMate();
        }

        long timeRemain = Math.max(0, levelTime - (int) (System.currentTimeMillis() - timeStart));
        try {
            if (timeRemain > 0) {
                Thread.sleep(timeRemain);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        final int moveFinal = move;
        final boolean playerLostFinal = playerLost;
        Minecraft.getInstance().submitAsync(() -> ClientPacketDistributor.sendToServer(new CChessToServerPackage(message.pos(), moveFinal, maidLost, playerLostFinal)));
    }

    public static void handleCheckSchedulePos(CheckSchedulePosPacket message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        if (mc.gui.screen() instanceof AbstractMaidContainerGui<?> parent) {
            mc.gui.setScreen(new CheckSchedulePosGui(parent, Component.translatable(message.tips())));
        }
    }

    public static void handleFoxScroll(FoxScrollPackage message) {
        Minecraft.getInstance().setScreenAndShow(new FoxScrollScreen(message.data()));
    }

    public static void handleGomokuClient(GomokuClientPackage message) {
        Point aiPoint = MaidGomokuAI.getService(message.count()).getPoint(message.chessData().toArray(new byte[15][]), message.point());
        int time = (int) (Math.random() * 1250) + 250;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Minecraft.getInstance().submitAsync(() -> ClientPacketDistributor.sendToServer(new GomokuServerPackage(message.pos(), aiPoint)));
    }

    public static void handleItemBreak(ItemBreakPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity e = mc.level.getEntity(message.id());
        if (e instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
            livingEntity.breakItem(message.item());
        }
    }

    public static void handleMaidAnimation(MaidAnimationPackage message) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        if (level.getEntity(message.maidId()) instanceof EntityMaid maid) {
            maid.animationId = message.animationId();
            maid.animationRecordTime = System.currentTimeMillis();
            maid.shouldReset = true;
        }
    }

    public static void handleOpenBeaconGui(OpenBeaconGuiPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        BlockEntity te = mc.level.getBlockEntity(message.pos());
        if (mc.player != null && mc.player.isAlive() && te instanceof TileEntityMaidBeacon) {
            mc.gui.setScreen(new MaidBeaconGui((TileEntityMaidBeacon) te));
        }
    }

    public static void handleOpenChairGui(OpenChairGuiPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity e = mc.level.getEntity(message.id());
        if (mc.player != null && mc.player.isAlive() && e instanceof EntityChair chair && e.isAlive()) {
            CacheIconManager.openChairModelGui(chair);
        }
    }

    public static void handleOpenPlayerInventory(OpenPlayerInventoryPackage message) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (message.action() == OpenPlayerInventoryPackage.OPEN_PLAYER_INVENTORY) {
            Minecraft.getInstance().setScreenAndShow(new InventoryScreen(player));
        }
    }

    public static void handleOpenSwitcherGui(OpenSwitcherGuiPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        BlockEntity te = mc.level.getBlockEntity(message.pos());
        if (mc.player != null && mc.player.isAlive() && te instanceof TileEntityModelSwitcher) {
            mc.gui.setScreen(new ModelSwitcherGui((TileEntityModelSwitcher) te));
        }
    }

    public static void handlePlayMaidSoundAtPos(PlayMaidSoundAtPosPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        SoundEvent event = BuiltInRegistries.SOUND_EVENT.getValue(message.soundEvent());
        if (event == null) {
            return;
        }
        mc.getSoundManager().play(new MaidSoundInstanceAtPos(event, message.id(),
                message.x(), message.y(), message.z(), message.volume(), message.pitch()));
    }

    public static void handlePlayMaidSound(PlayMaidSoundPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity entity = mc.level.getEntity(message.entityId());
        if (!(entity instanceof EntityMaid maid)) {
            return;
        }
        SoundEvent event = BuiltInRegistries.SOUND_EVENT.getValue(message.soundEvent());
        if (event == null) {
            return;
        }
        mc.getSoundManager().play(new MaidSoundInstance(event, message.id(), maid));
    }

    public static void handleSendEffect(SendEffectPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity entity = mc.level.getEntity(message.id());
        if (entity instanceof EntityMaid maid && maid.isAlive()) {
            maid.setEffects(message.effects().stream().map(SendEffectPackage.EffectData::new).toList());
        }
    }

    public static void handleSpawnParticle(SpawnParticlePackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity e = mc.level.getEntity(message.entityId());
        if (e instanceof EntityMaid maid && e.isAlive()) {
            switch (message.particleType()) {
                case EXPLOSION:
                    maid.spawnExplosionParticle();
                    return;
                case BUBBLE:
                    maid.spawnBubbleParticle();
                    return;
                case HEART:
                    maid.spawnHeartParticle();
                    return;
                case RANK_UP:
                    maid.spawnRankUpParticle();
                    return;
                case HEAL:
                    maid.spawnRestoreHealthParticle(maid.getRandom().nextInt(3) + 7);
                    return;
                default:
            }
        }
    }

    public static void handleSpawnParticleDelay(SpawnParticlePackage message) {
        try {
            Thread.sleep(message.delayTicks() * 50L);
            Minecraft.getInstance().submitAsync(() -> handleSpawnParticle(message));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void handleSyncBauble(SyncBaublePackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity entity = mc.level.getEntity(message.entityId());
        if (entity instanceof EntityMaid maid) {
            BaubleItemHandler maidBauble = maid.getMaidBauble();
            if (message.isFull()) {
                maidBauble.clearAll();
            }
            message.baubles().forEach(maidBauble::setStackInSlot);
        }
    }

    public static void handleSyncData(SyncDataPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }
        mc.player.setData(InitDataAttachment.POWER_NUM, new PowerAttachment(message.power()));
        mc.player.setData(InitDataAttachment.MAID_NUM, new MaidNumAttachment(message.maidNum()));
    }

    public static void handleSyncMaidArea(SyncMaidAreaPackage message) {
        MaidAreaRenderEvent.addSchedulePos(message.id(), message.schedulePos());
    }

    public static void handleSyncYsmMaidData(SyncYsmMaidDataPackage message) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        Entity entity = level.getEntity(message.entityId());
        if (!(entity instanceof EntityMaid maid)) {
            return;
        }
        NeoForge.EVENT_BUS.post(new UpdateRemoteStructEvent(maid, message.roamingVars()));
        if (message.isRouletteAnimPlaying()) {
            maid.playRouletteAnim(message.rouletteAnim());
        } else {
            maid.stopRouletteAnim();
        }
    }

    public static void handleWChessToClient(WChessToClientPackage message) {
        int levelTime = 1000;
        long timeStart = System.currentTimeMillis();
        int move = 0;

        Position position = new Position();
        position.fromFen(message.fenData());

        boolean maidLost = WChessUtil.isMaid(position) && position.isMate();
        boolean playerLost = false;
        if (!maidLost) {
            move = new Search(position, 12).searchMain(levelTime);
            playerLost = position.makeMove(move) && WChessUtil.isPlayer(position) && position.isMate();
        }

        long timeRemain = Math.max(0, levelTime - (int) (System.currentTimeMillis() - timeStart));
        try {
            if (timeRemain > 0) {
                Thread.sleep(timeRemain);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        final int moveFinal = move;
        final boolean playerLostFinal = playerLost;
        Minecraft.getInstance().submitAsync(() -> ClientPacketDistributor.sendToServer(new WChessToServerPackage(message.pos(), moveFinal, maidLost, playerLostFinal)));
    }

    public static void handleSyncAISites(SyncAISitesPacket message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gui.screen() instanceof LLMSiteEditorScreen editor) {
            editor.getParentHub().reopenSelf(message.llmSites(), message.ttsSites());
        } else if (mc.gui.screen() instanceof TTSSiteEditorScreen editor) {
            editor.getParentHub().reopenSelf(message.llmSites(), message.ttsSites());
        } else if (mc.gui.screen() instanceof AIChatSettingsHubScreen hubScreen) {
            hubScreen.reopenSelf(message.llmSites(), message.ttsSites());
        } else if (mc.gui.screen() instanceof AIChatScreen screen) {
            mc.gui.setScreen(new AIChatSettingsLLMSiteScreen(screen, message.llmSites(), message.ttsSites(), message.insufficientPermissions()));
        } else {
            mc.gui.setScreen(AIChatSettingsHubScreen.openDefault(null, message.llmSites(), message.ttsSites(), message.insufficientPermissions()));
        }
    }

    public static void handleSyncMaidAIData(SyncMaidAIDataPacket message) {
        ClientLevel level = Minecraft.getInstance().level;
        LocalPlayer player = Minecraft.getInstance().player;
        if (level == null || player == null) {
            Minecraft.getInstance().gui.setScreen(null);
            return;
        }
        Entity entity = level.getEntity(message.entityId());
        if (entity instanceof EntityMaid maid) {
            maid.getAiChatManager().readFromTag(message.configData());

            AIChatScreen chatScreen = new AIChatScreen(maid);
            chatScreen.updateTokens(message.currentTokens(), message.maxTokens());
            Minecraft.getInstance().setScreenAndShow(chatScreen);
        } else {
            Minecraft.getInstance().gui.setScreen(null);
        }
    }

    public static void handleTTSAudioToClient(TTSAudioToClientPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity entity = mc.level.getEntity(message.maidId());
        if (!(entity instanceof EntityMaid maid)) {
            return;
        }
        if (maid.isAlive()) {
            MaidAISoundInstance instance = new MaidAISoundInstance(maid, message.data());
            Minecraft.getInstance().getSoundManager().play(instance);
        }
    }
}
