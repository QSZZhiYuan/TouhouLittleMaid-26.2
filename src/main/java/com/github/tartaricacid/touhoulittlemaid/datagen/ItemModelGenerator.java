package com.github.tartaricacid.touhoulittlemaid.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.DataProvider;

import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;

public class ItemModelGenerator implements DataProvider {
    public ItemModelGenerator(PackOutput output) {
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public String getName() {
        return "Item Models: Touhou Little Maid";
    }
}
