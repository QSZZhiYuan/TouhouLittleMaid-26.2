package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.core.registries.Registries;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.compat.stub.YsmCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.YsmMaidInfo;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.github.tartaricacid.touhoulittlemaid.util.ComponentSerializerHelper;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.ENTITY_ID_TAG_NAME;
import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.MODEL_ID_TAG_NAME;

public class ItemGarageKit extends BlockItem {
    private static final String DEFAULT_ENTITY_ID = "touhou_little_maid:maid";
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    private static final CustomData DEFAULT_DATA = getDefaultData();

    public ItemGarageKit(Item.Properties properties) {
        super(InitBlocks.GARAGE_KIT.get(), (properties).stacksTo(1));
    }

    public static CustomData getMaidData(ItemStack stack) {
        return Objects.requireNonNullElse(stack.get(InitDataComponent.MAID_INFO), DEFAULT_DATA);
    }

    private static CustomData getDefaultData() {
        CompoundTag data = new CompoundTag();
        data.putString(ENTITY_ID_TAG_NAME, DEFAULT_ENTITY_ID);
        data.putString(MODEL_ID_TAG_NAME, DEFAULT_MODEL_ID);
        // 榛樿鏁版嵁闇€瑕佸己鍒舵寚锟?YSM 娓叉煋涓虹┖
        data.putBoolean(EntityMaid.IS_YSM_MODEL_TAG, false);
        return CustomData.of(data);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Component getName(ItemStack stack) {
        // 浠呭湪瀹㈡埛绔坊鍔犺繖涓悕锟?
        if (FMLEnvironment.getDist() == Dist.CLIENT && Minecraft.getInstance().level != null) {
            // 鎵嬪姙鍚嶅瓧鍓嶇紑
            MutableComponent prefix = Component.translatable("block.touhou_little_maid.garage_kit.prefix");
            CustomData data = getMaidData(stack);

            String entityId = data.copyTag().read(Codec.STRING.fieldOf(ENTITY_ID_TAG_NAME)).orElse(DEFAULT_ENTITY_ID);
            // 濡傛灉鏄叾浠栧疄浣擄紝閭ｄ箞涓嶉渶瑕佹樉绀?model id
            if (!entityId.equals(DEFAULT_ENTITY_ID)) {
                EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse(entityId));
                return prefix.append(entityType.getDescription());
            }

            // 浼樺厛浣跨敤 YSM 妯″瀷鍚嶇О
            if (YsmCompat.isInstalled()) {
                YsmMaidInfo ysmMaidInfo = YsmCompat.getYsmMaidInfo(data.copyTag());
                if (ysmMaidInfo.isYsmModel()) {
                    MutableComponent name = ComponentSerializerHelper.fromJson(ysmMaidInfo.name(), Minecraft.getInstance().level.registryAccess());
                    if (name == null || name.equals(Component.empty())) {
                        return prefix.append(ysmMaidInfo.modelId());
                    }
                    return prefix.append(name);
                }
            }

            // 鐒跺悗鎵嶆槸榛樿妯″瀷锟?
            String modelId = data.copyTag().read(Codec.STRING.fieldOf(MODEL_ID_TAG_NAME)).orElse(DEFAULT_MODEL_ID);
            MaidModelInfo info = CustomPackLoader.MAID_MODELS.getInfo(modelId).orElse(null);
            if (info != null) {
                return prefix.append(ParseI18n.parse(info.getName()));
            }
            return super.getName(stack);
        }
        return super.getName(stack);
    }
}
