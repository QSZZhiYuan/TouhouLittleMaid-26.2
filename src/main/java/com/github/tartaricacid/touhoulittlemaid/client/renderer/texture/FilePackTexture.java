package com.github.tartaricacid.touhoulittlemaid.client.renderer.texture;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import net.minecraft.resources.Identifier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilePackTexture extends SizeTexture {
    private final Identifier texturePath;
    private final Path rootPath;
    private int width = 16;
    private int height = 16;

    public FilePackTexture(Path rootPath, Identifier texturePath) {
        this.rootPath = rootPath;
        this.texturePath = texturePath;
        this.doLoad();
    }

    @Override
    public boolean isExist() {
        File textureFile = rootPath.resolve("assets").resolve(texturePath.getNamespace()).resolve(texturePath.getPath()).toFile();
        return textureFile.isFile();
    }

    private void doLoad() {
        File textureFile = rootPath.resolve("assets").resolve(texturePath.getNamespace()).resolve(texturePath.getPath()).toFile();
        if (textureFile.isFile()) {
            try (InputStream stream = Files.newInputStream(textureFile.toPath())) {
                NativeImage imageIn = NativeImage.read(stream);
                width = imageIn.getWidth();
                height = imageIn.getHeight();
                GpuDevice device = RenderSystem.getDevice();
                this.close();
                this.texture = device.createTexture(texturePath::toString, 5, GpuFormat.RGBA8_UNORM, width, height, 1, 1);
                this.sampler = RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST);
                this.textureView = device.createTextureView(this.texture);
                device.createCommandEncoder().writeToTexture(this.texture, imageIn);
                imageIn.close();
            } catch (IOException e) {
                TouhouLittleMaid.LOGGER.error("Failed to load file texture {}", texturePath, e);
            }
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
