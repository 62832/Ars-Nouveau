package com.hollingsworth.arsnouveau.common.datagen;


import com.hollingsworth.arsnouveau.ArsNouveau;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class PlacedFeatureTagProvider extends TagsProvider<PlacedFeature> {
    public static TagKey<PlacedFeature> ARCHWOOD_TREES = TagKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ArsNouveau.MODID, "archwood_trees"));
    public static TagKey<PlacedFeature> SOURCE_BERRIES = TagKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ArsNouveau.MODID, "source_berries"));

    public PlacedFeatureTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, Registries.PLACED_FEATURE, pProvider, ArsNouveau.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ARCHWOOD_TREES);
        this.tag(SOURCE_BERRIES);
    }
}
