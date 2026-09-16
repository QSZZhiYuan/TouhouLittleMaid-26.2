package com.github.tartaricacid.touhoulittlemaid.client.renderer.texture;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;

public class CacheIconTexture extends AbstractTexture {
    private final Identifier modelId;
    private NativeImage imageIn;

    public CacheIconTexture(Identifier modelId, NativeImage imageIn) {
        this.modelId = modelId;
        this.imageIn = imageIn;
        this.doLoad();
    }

    private void doLoad() {
        if (imageIn == null) {
            return;
        }
        int width = imageIn.getWidth();
        int height = imageIn.getHeight();
        GpuDevice device = RenderSystem.getDevice();
        this.close();
        this.texture = device.createTexture(modelId::toString, 5, GpuFormat.RGBA8_UNORM, width, height, 1, 1);
        this.sampler = RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST);
        this.textureView = device.createTextureView(this.texture);
        device.createCommandEncoder().writeToTexture(this.texture, imageIn);
        // 释放内存
        imageIn = null;
    }

    public Identifier getModelId() {
        return modelId;
    }
}
