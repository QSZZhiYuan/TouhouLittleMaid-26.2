package com.github.tartaricacid.touhoulittlemaid.client.renderer.item;

import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class ReplaceableResolvedModel implements ResolvedModel {
    private final ResolvedModel rawResolvedModel;
    private final ResolvedModel replacedResolvedModel;
    private final Supplier<Boolean> isReplace;

    public ReplaceableResolvedModel(ResolvedModel rawResolvedModel, ResolvedModel replacedResolvedModel, Supplier<Boolean> isReplace) {
        this.rawResolvedModel = rawResolvedModel;
        this.replacedResolvedModel = replacedResolvedModel;
        this.isReplace = isReplace;
    }

    @Override
    public UnbakedModel wrapped() {
        if (isReplace.get()) {
            return this.replacedResolvedModel.wrapped();
        } else {
            return this.rawResolvedModel.wrapped();
        }
    }

    @Override
    public @Nullable ResolvedModel parent() {
        if (isReplace.get()) {
            return this.replacedResolvedModel.parent();
        } else {
            return this.rawResolvedModel.parent();
        }
    }

    @Override
    public String debugName() {
        return "ReplaceableResolvedModel";
    }
}
