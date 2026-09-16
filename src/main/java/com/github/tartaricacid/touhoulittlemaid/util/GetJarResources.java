package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.neoforged.fml.ModList;
import net.neoforged.fml.jarcontents.JarContents;
import net.neoforged.fml.jarcontents.JarResource;
import net.neoforged.neoforgespi.language.IModFileInfo;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class GetJarResources {
    private GetJarResources() {
    }

    @Nullable
    private static JarContents getModJarContents() {
        IModFileInfo modFileInfo = ModList.get().getModFileById(TouhouLittleMaid.MOD_ID);
        if (modFileInfo == null) {
            TouhouLittleMaid.LOGGER.error("Mod file not found for {}", TouhouLittleMaid.MOD_ID);
            return null;
        }
        return modFileInfo.getFile().getContents();
    }

    private static String toRelativePath(String filePath) {
        return filePath.startsWith("/") ? filePath.substring(1) : filePath;
    }

    /**
     * 复制本模组的文件到指定文件夹
     *
     * @param filePath jar 里面的文件地址
     * @param destPath 想要复制到的目录
     * @param fileName 复制后的文件名
     */
    public static void copyTouhouLittleMaidFile(String filePath, Path destPath, String fileName) {
        JarContents contents = getModJarContents();
        if (contents == null) {
            return;
        }
        String relativePath = toRelativePath(filePath);
        try (InputStream in = contents.openFile(relativePath)) {
            if (in != null) {
                Files.copy(in, destPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to copy built-in resource {}", filePath, e);
        }
    }

    @Nullable
    public static InputStream readTouhouLittleMaidFile(String filePath) {
        JarContents contents = getModJarContents();
        if (contents == null) {
            return null;
        }
        String relativePath = toRelativePath(filePath);
        try {
            return contents.openFile(relativePath);
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to read built-in resource {}", filePath, e);
            return null;
        }
    }

    public static void copyFolder(String sourcePath, Path targetPath) throws IOException {
        JarContents contents = getModJarContents();
        if (contents == null) {
            return;
        }
        String folder = toRelativePath(sourcePath);
        String prefix = folder.endsWith("/") ? folder : folder + "/";
        contents.visitContent(folder, (relativePath, resource) -> {
            try {
                String subPath = relativePath.startsWith(prefix) ? relativePath.substring(prefix.length()) : relativePath;
                Path target = targetPath.resolve(subPath);
                Path parentDir = target.getParent();
                if (parentDir != null && !Files.isDirectory(parentDir)) {
                    Files.createDirectories(parentDir);
                }
                try (InputStream in = resource.open()) {
                    Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                TouhouLittleMaid.LOGGER.error("Failed to copy file {} to target: {}", relativePath, e.getMessage());
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error("Unexpected error during file copy: {}", e.getMessage());
            }
        });
    }
}
