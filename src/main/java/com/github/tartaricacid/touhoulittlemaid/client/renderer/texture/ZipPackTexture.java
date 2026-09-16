package com.github.tartaricacid.touhoulittlemaid.client.renderer.texture;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import net.minecraft.resources.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipPackTexture extends SizeTexture {
    private final Identifier texturePath;
    private final Path zipFilePath;
    private int width = 16;
    private int height = 16;

    public ZipPackTexture(String zipFilePath, Identifier texturePath) {
        this.zipFilePath = Paths.get(zipFilePath);
        this.texturePath = texturePath;
        this.doLoad();
    }

    @Override
    public boolean isExist() {
        try (ZipFile zipFile = new ZipFile(zipFilePath.toFile())) {
            ZipEntry entry = zipFile.getEntry(String.format("assets/%s/%s", texturePath.getNamespace(), texturePath.getPath()));
            return entry != null;
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to inspect zip texture {}", texturePath, e);
        }
        return false;
    }

    private void doLoad() {
        try (ZipFile zipFile = new ZipFile(zipFilePath.toFile())) {
            ZipEntry entry = zipFile.getEntry(String.format("assets/%s/%s", texturePath.getNamespace(), texturePath.getPath()));
            if (entry == null) {
                return;
            }
            try (InputStream stream = zipFile.getInputStream(entry)) {
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
                TouhouLittleMaid.LOGGER.error("Failed to load zip texture {}", texturePath, e);
            }
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to open zip texture {}", texturePath, e);
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
