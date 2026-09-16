package com.github.tartaricacid.touhoulittlemaid.client.animation;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.inner.InnerAnimation;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.IModelInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.io.IOUtils;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.annotation.Nullable;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@OnlyIn(Dist.CLIENT)
public class CustomJsAnimationManger {
    public static final ScriptEngine NASHORN = new NashornScriptEngineFactory().getScriptEngine();
    private static final Map<Identifier, Object> CUSTOM_ANIMATION_MAP = Maps.newHashMap();

    @Nullable
    public static List<Object> getCustomAnimation(Path rootPath, IModelInfo item) {
        List<Object> animations = Lists.newArrayList();
        if (item.getAnimation() != null && !item.getAnimation().isEmpty()) {
            for (Identifier res : item.getAnimation()) {
                Object animation = CustomJsAnimationManger.getCustomAnimation(rootPath, res);
                if (animation != null) {
                    animations.add(animation);
                }
            }
            return animations;
        }
        return null;
    }

    @Nullable
    public static List<Object> getCustomAnimation(ZipFile zipFile, IModelInfo item) {
        List<Object> animations = Lists.newArrayList();
        if (item.getAnimation() != null && !item.getAnimation().isEmpty()) {
            for (Identifier res : item.getAnimation()) {
                Object animation = CustomJsAnimationManger.getCustomAnimation(zipFile, res);
                if (animation != null) {
                    animations.add(animation);
                }
            }
            return animations;
        }
        return null;
    }

    @Nullable
    private static Object getCustomAnimation(Path rootPath, @Nullable Identifier Identifier) {
        if (Identifier == null) {
            return null;
        }
        if (CUSTOM_ANIMATION_MAP.containsKey(Identifier)) {
            return CUSTOM_ANIMATION_MAP.get(Identifier);
        }
        if (InnerAnimation.containsKey(Identifier)) {
            return InnerAnimation.get(Identifier);
        }
        File file = rootPath.resolve("assets").resolve(Identifier.getNamespace()).resolve(Identifier.getPath()).toFile();
        if (!file.isFile()) {
            return null;
        }
        try (InputStream stream = new FileInputStream(file)) {
            Bindings bindings = NASHORN.createBindings();
            Object scriptObject = NASHORN.eval(IOUtils.toString(stream, StandardCharsets.UTF_8), bindings);
            CUSTOM_ANIMATION_MAP.put(Identifier, scriptObject);
            return scriptObject;
        } catch (IOException | ScriptException e) {
            TouhouLittleMaid.LOGGER.error("Failed to load custom animation {}", Identifier, e);
        }
        return null;
    }

    @Nullable
    private static Object getCustomAnimation(ZipFile zipFile, @Nullable Identifier Identifier) {
        if (Identifier == null) {
            return null;
        }
        if (CUSTOM_ANIMATION_MAP.containsKey(Identifier)) {
            return CUSTOM_ANIMATION_MAP.get(Identifier);
        }
        if (InnerAnimation.containsKey(Identifier)) {
            return InnerAnimation.get(Identifier);
        }
        ZipEntry entry = zipFile.getEntry(String.format("assets/%s/%s", Identifier.getNamespace(), Identifier.getPath()));
        if (entry == null) {
            return null;
        }
        try (InputStream stream = zipFile.getInputStream(entry)) {
            Bindings bindings = NASHORN.createBindings();
            Object scriptObject = NASHORN.eval(IOUtils.toString(stream, StandardCharsets.UTF_8), bindings);
            CUSTOM_ANIMATION_MAP.put(Identifier, scriptObject);
            return scriptObject;
        } catch (IOException | ScriptException e) {
            TouhouLittleMaid.LOGGER.error("Failed to load custom animation {}", Identifier, e);
        }
        return null;
    }

    public static void clearAll() {
        CUSTOM_ANIMATION_MAP.clear();
    }
}
