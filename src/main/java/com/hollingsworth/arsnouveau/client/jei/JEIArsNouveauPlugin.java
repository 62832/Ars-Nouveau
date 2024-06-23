package com.hollingsworth.arsnouveau.client.jei;

import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.ArmorUpgradeRecipe;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantmentRecipe;
import com.hollingsworth.arsnouveau.client.container.IAutoFillTerminal;
import com.hollingsworth.arsnouveau.common.crafting.recipes.CrushRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.DyeRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.GlyphRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ImbuementRecipe;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectCrush;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.RecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIArsNouveauPlugin implements IModPlugin {
    public static final RecipeType<GlyphRecipe> GLYPH_RECIPE_TYPE = RecipeType.create(ArsNouveau.MODID, "glyph_recipe", GlyphRecipe.class);
    public static final RecipeType<EnchantingApparatusRecipe> ENCHANTING_APP_RECIPE_TYPE = RecipeType.create(ArsNouveau.MODID, "enchanting_apparatus", EnchantingApparatusRecipe.class);
    public static final RecipeType<EnchantmentRecipe> ENCHANTING_RECIPE_TYPE = RecipeType.create(ArsNouveau.MODID, "enchantment_apparatus", EnchantmentRecipe.class);
    public static final RecipeType<ArmorUpgradeRecipe> ARMOR_RECIPE_TYPE = RecipeType.create(ArsNouveau.MODID, "armor_upgrade", ArmorUpgradeRecipe.class);

    public static final RecipeType<ImbuementRecipe> IMBUEMENT_RECIPE_TYPE = RecipeType.create(ArsNouveau.MODID, "imbuement", ImbuementRecipe.class);
    public static final RecipeType<CrushRecipe> CRUSH_RECIPE_TYPE = RecipeType.create(ArsNouveau.MODID, "crush", CrushRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ArsNouveau.prefix( "main");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new GlyphRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new CrushRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new ImbuementRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new EnchantingApparatusRecipeCategory<>(registry.getJeiHelpers().getGuiHelper()),
                new ApparatusEnchantingRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new ArmorUpgradeRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        List<GlyphRecipe> recipeList = new ArrayList<>();
        List<EnchantingApparatusRecipe> apparatus = new ArrayList<>();
        List<EnchantmentRecipe> enchantments = new ArrayList<>();
        List<CrushRecipe> crushRecipes = new ArrayList<>();
        List<ArmorUpgradeRecipe> armorUpgrades = new ArrayList<>();
        List<ImbuementRecipe> imbuementRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeRegistry.IMBUEMENT_TYPE.get()).stream().map(RecipeHolder::value).toList();
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        for (Recipe<?> i : manager.getRecipes().stream().map(RecipeHolder::value).toList()) {
            if (i instanceof GlyphRecipe glyphRecipe) {
                recipeList.add(glyphRecipe);
            }
            if (i instanceof EnchantmentRecipe enchantmentRecipe) {
                enchantments.add(enchantmentRecipe);
            } else if (i instanceof ArmorUpgradeRecipe upgradeRecipe) {
                armorUpgrades.add(upgradeRecipe);
            } else if (i instanceof EnchantingApparatusRecipe enchantingApparatusRecipe && !enchantingApparatusRecipe.excludeJei()) {
                apparatus.add(enchantingApparatusRecipe);
            }
            if (i instanceof CrushRecipe crushRecipe) {
                crushRecipes.add(crushRecipe);
            }
        }
        registry.addRecipes(GLYPH_RECIPE_TYPE, recipeList);
        registry.addRecipes(CRUSH_RECIPE_TYPE, crushRecipes);
        registry.addRecipes(ENCHANTING_APP_RECIPE_TYPE, apparatus);
        registry.addRecipes(ENCHANTING_RECIPE_TYPE, enchantments);
        registry.addRecipes(IMBUEMENT_RECIPE_TYPE, imbuementRecipes);
        registry.addRecipes(ARMOR_RECIPE_TYPE, armorUpgrades);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(BlockRegistry.SCRIBES_BLOCK), GLYPH_RECIPE_TYPE);
        registry.addRecipeCatalyst(new ItemStack(EffectCrush.INSTANCE.glyphItem), CRUSH_RECIPE_TYPE);
        registry.addRecipeCatalyst(new ItemStack(BlockRegistry.IMBUEMENT_BLOCK), IMBUEMENT_RECIPE_TYPE);
        registry.addRecipeCatalyst(new ItemStack(BlockRegistry.ENCHANTING_APP_BLOCK), ENCHANTING_APP_RECIPE_TYPE);
        registry.addRecipeCatalyst(new ItemStack(BlockRegistry.ENCHANTING_APP_BLOCK), ENCHANTING_RECIPE_TYPE);
        registry.addRecipeCatalyst(new ItemStack(BlockRegistry.ENCHANTING_APP_BLOCK), ARMOR_RECIPE_TYPE);

    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
//        registration.addRecipeClickArea(CraftingTerminalScreen.class, 100, 125, 28, 23, new RecipeType[] { RecipeTypes.CRAFTING });
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        CraftingTerminalTransferHandler.registerTransferHandlers(registration);
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addExtension(DyeRecipe.class, new DyeRecipeCategory());
    }

    private static IJeiRuntime jeiRuntime;

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        JEIArsNouveauPlugin.jeiRuntime = jeiRuntime;
    }

    static {
        IAutoFillTerminal.updateSearch.add(new IAutoFillTerminal.ISearchHandler() {

            @Override
            public void setSearch(String text) {
                if (jeiRuntime != null) {
                    if (jeiRuntime.getIngredientFilter() != null) {
                        jeiRuntime.getIngredientFilter().setFilterText(text);
                    }
                }
            }

            @Override
            public String getSearch() {
                if (jeiRuntime != null) {
                    if (jeiRuntime.getIngredientFilter() != null) {
                        return jeiRuntime.getIngredientFilter().getFilterText();
                    }
                }
                return "";
            }

            @Override
            public String getName() {
                return "JEI";
            }
        });
    }
}