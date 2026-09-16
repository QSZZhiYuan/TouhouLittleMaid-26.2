package com.github.tartaricacid.simplebedrockmodel.client.manager;

import com.github.tartaricacid.simplebedrockmodel.SimpleBedrockModel;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.AbstractBedrockEntityModel;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

import java.util.Set;

@SuppressWarnings({"unchecked", "rawtypes"})
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public class BedrockEntityModelRegister<T extends AbstractBedrockEntityModel<? extends EntityRenderState>> {
    public static BedrockEntityModelRegister INSTANCE = null;
    private final BedrockEntityModelSet<T> modelSet;

    private BedrockEntityModelRegister(BedrockEntityModelSet<T> modelSet) {
        this.modelSet = modelSet;
    }

    @SubscribeEvent
    public static void onAddClientReloadListenersEvent(AddClientReloadListenersEvent event) {
        INSTANCE = new BedrockEntityModelRegister<>(new BedrockEntityModelSet<>());
        ModLoader.postEvent(new BedrockEntityModelRegisterEvent(INSTANCE.modelSet));
        INSTANCE.modelSet.immutableKnowLocations();
        event.addListener(
            Identifier.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "bedrock_model_set"),
            INSTANCE.modelSet
        );
    }

    public AbstractBedrockEntityModel<? extends EntityRenderState> getModel(Identifier location) {
        return modelSet.getModels().get(location);
    }

    public Set<Identifier> getAllModelKeys() {
        return modelSet.getModels().keySet();
    }
}
