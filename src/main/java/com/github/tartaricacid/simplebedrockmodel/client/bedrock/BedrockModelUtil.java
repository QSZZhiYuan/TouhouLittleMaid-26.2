package com.github.tartaricacid.simplebedrockmodel.client.bedrock;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.CubesItem;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.FaceItem;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.FaceUVsItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class BedrockModelUtil {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(Identifier.class, new IdentifierTypeAdapter())
            .registerTypeAdapter(CubesItem.class, new CubesItem.Deserializer())
            .create();

    /**
     * 替代 26.2 中移除的 Identifier.Serializer
     */
    private static class IdentifierTypeAdapter implements com.google.gson.JsonSerializer<Identifier>, com.google.gson.JsonDeserializer<Identifier> {
        @Override
        public Identifier deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
            return Identifier.parse(json.getAsString());
        }

        @Override
        public com.google.gson.JsonElement serialize(Identifier src, java.lang.reflect.Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(src.toString());
        }
    }

    public static FaceUVsItem singleSouthFace() {
        return new FaceUVsItem(emptyFace(), emptyFace(), emptyFace(), single16xFace(), emptyFace(), emptyFace());
    }

    public static FaceItem single16xFace() {
        return new FaceItem(new float[]{0, 0}, new float[]{16, 16});
    }

    public static FaceItem emptyFace() {
        return new FaceItem(new float[]{0, 0}, new float[]{0, 0});
    }
}
