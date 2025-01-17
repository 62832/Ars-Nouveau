package com.hollingsworth.arsnouveau.common.datagen;

import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.setup.registry.DamageTypesRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DamageTypesProvider  extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, DamageTypesProvider::bootstrap);


    public static void bootstrap(BootstapContext<DamageType> ctx) {
        ctx.register(DamageTypesRegistry.COLD_SNAP, new DamageType("freeze", 0.1F));
        ctx.register(DamageTypesRegistry.FLARE, new DamageType("fire", 0.1F));
        ctx.register(DamageTypesRegistry.CRUSH, new DamageType("fire", 0.1F));
        ctx.register(DamageTypesRegistry.WINDSHEAR, new DamageType("fire", 0.1F));

    }

    public DamageTypesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ArsNouveau.MODID));
    }

    @Override
    @NotNull
    public String getName() {
        return "Ars Nouveau's Damage Type Data";
    }




    public static class DamageTypesTagsProvider extends DamageTypeTagsProvider {

        public DamageTypesTagsProvider(PackOutput pPackOutput, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(pPackOutput, provider, ArsNouveau.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {

            tag(DamageTypeTags.IS_FIRE).addOptional(DamageTypesRegistry.FLARE.location());
            tag(DamageTypeTags.IS_FREEZING).addOptional(DamageTypesRegistry.COLD_SNAP.location());
            tag(DamageTypeTags.BYPASSES_ARMOR).addOptional(DamageTypesRegistry.CRUSH.location()).addOptional(DamageTypesRegistry.WINDSHEAR.location());
            tag(DamageTypeTags.IS_FALL).addOptional(DamageTypesRegistry.WINDSHEAR.location());
        }
    }

}
