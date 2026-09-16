package com.github.tartaricacid.touhoulittlemaid.client.renderer.texture;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.client.decoder.GifDecoder;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import net.minecraft.client.renderer.texture.TickableTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class GifTexture extends SizeTexture implements TickableTexture {
    private final Identifier texturePath;
    private NativeImage[] frames;
    private int[] frameDelays;
    private int currentFrame = 0;
    private int currentFrameDelay = 0;
    private int width = 16;
    private int height = 16;

    public GifTexture(Identifier texturePath) {
        this.texturePath = texturePath;
        this.load();
    }

    @SuppressWarnings("all")
    private void load() {
        ResourceManager manager = net.minecraft.client.Minecraft.getInstance().getResourceManager();
        try (InputStream stream = manager.open(this.texturePath)) {
            GifDecoder decoder = new GifDecoder();
            decoder.read(stream);

            int totalFrames = decoder.getFrameCount();
            Dimension frameSize = decoder.getFrameSize();
            this.frames = new NativeImage[totalFrames];
            this.frameDelays = new int[totalFrames];
            this.width = frameSize.width;
            this.height = frameSize.height;

            // 让图片学习原版序列帧竖向排列
            for (int i = 0; i < totalFrames; i++) {
                NativeImage nativeImage = new NativeImage(this.width, this.height, true);
                BufferedImage image = decoder.getFrame(i);
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        int argb = image.getRGB(x, y);
                        int a = (argb >> 24) & 0xFF;
                        int r = (argb >> 16) & 0xFF;
                        int g = (argb >> 8) & 0xFF;
                        int b = argb & 0xFF;

                        nativeImage.setPixel(x, y, (a << 24) | (b << 16) | (g << 8) | r);
                    }
                }
                this.frames[i] = nativeImage;
                this.frameDelays[i] = Math.max(decoder.getDelay(i) / 50, 1);
            }
            // 上传第一帧
            this.uploadFrame(this.frames[0]);
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Failed to load gif texture: {}", this.texturePath, e);
        }
    }

    private void uploadFrame(NativeImage frame) {
        GpuDevice device = RenderSystem.getDevice();
        if (this.texture == null) {
            this.close();
            this.texture = device.createTexture(texturePath::toString, 5, GpuFormat.RGBA8_UNORM, width, height, 1, 1);
            this.sampler = RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST);
            this.textureView = device.createTextureView(this.texture);
        }
        device.createCommandEncoder().writeToTexture(this.texture, frame);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public boolean isExist() {
        return true;
    }

    @Override
    public void tick() {
        if (frames == null || frames.length == 0) {
            return;
        }
        currentFrameDelay++;
        if (currentFrameDelay >= frameDelays[currentFrame]) {
            currentFrameDelay = 0;
            currentFrame = (currentFrame + 1) % frames.length;
            uploadFrame(frames[currentFrame]);
        }
    }
}
