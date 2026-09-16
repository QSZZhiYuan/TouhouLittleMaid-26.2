package com.github.tartaricacid.touhoulittlemaid.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.core.HolderLookup;

/**
 * 替代 26.2 中被移除的 Component.Serializer
 * 使用 ComponentSerialization.CODEC 进行 JSON 序列化/反序列化
 */
public final class ComponentSerializerHelper {
    private ComponentSerializerHelper() {
    }

    public static String toJson(Component component, HolderLookup.Provider provider) {
        return ComponentSerialization.CODEC.encodeStart(provider.createSerializationContext(JsonOps.INSTANCE), component)
                .map(JsonElement::toString)
                .result()
                .orElse("{}");
    }

    public static MutableComponent fromJson(String json, HolderLookup.Provider provider) {
        try {
            JsonElement element = JsonParser.parseString(json);
            return ComponentSerialization.CODEC.parse(provider.createSerializationContext(JsonOps.INSTANCE), element)
                    .map(Component::copy)
                    .result()
                    .orElse(Component.empty());
        } catch (Exception e) {
            return Component.empty();
        }
    }
}