package gregicadditions.recipes;

import gregicadditions.item.GAMetaItems;
import gregtech.api.GTValues;
import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.ModHandler;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.type.DustMaterial;
import gregtech.api.unification.material.type.IngotMaterial;
import gregtech.api.unification.material.type.Material;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.api.util.GTLog;
import gregtech.common.items.behaviors.TurbineRotorBehavior;
import gregtech.loaders.oreprocessing.OreRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;

import static gregicadditions.GAMaterials.FermentationBase;
import static gregicadditions.GAMaterials.GENERATE_METAL_CASING;
import static gregtech.api.unification.material.Materials.Lubricant;
import static gregtech.api.unification.material.Materials.Water;

public class RecipeHandler {

    public static void register() {
        OrePrefix.valueOf("gtMetalCasing").addProcessingHandler(IngotMaterial.class, RecipeHandler::processMetalCasing);
        OrePrefix.turbineBlade.addProcessingHandler(IngotMaterial.class, RecipeHandler::processTurbine);
        OrePrefix.dustImpure.addProcessingHandler(DustMaterial.class, RecipeHandler::processDirtyDust);
    }

    private static void processMetalCasing(OrePrefix prefix, IngotMaterial material) {
        if (material.hasFlag(GENERATE_METAL_CASING)) {
            ItemStack metalCasingStack = OreDictUnifier.get(prefix, material, 3);
            ModHandler.addShapedRecipe(String.format("metal_casing_%s", material), metalCasingStack,
                    "PhP", "PBP", "PwP",
                    'P', new UnificationEntry(OrePrefix.plate, material),
                    'B', new UnificationEntry(OrePrefix.frameGt, material));


            RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                    .input(OrePrefix.plate, material, 6)
                    .input(OrePrefix.frameGt, material, 1)
                    .outputs(metalCasingStack)
                    .EUt(8).duration(200)
                    .buildAndRegister();
        }
    }

    public static void processDirtyDust(OrePrefix dustImpurePrefix, DustMaterial dustMaterial) {
        GARecipeMaps.SIMPLE_ORE_WASHER.recipeBuilder().input(dustImpurePrefix, dustMaterial)
                .fluidInputs(Water.getFluid(100))
                .outputs(OreDictUnifier.get(OrePrefix.dust, dustMaterial)).buildAndRegister();
    }

    public static void processTurbine(OrePrefix toolPrefix, IngotMaterial material) {
        ItemStack hugeTurbineRotorStackForm = GAMetaItems.HUGE_TURBINE_ROTOR.getStackForm();
        ItemStack largeTurbineRotorStackForm = GAMetaItems.LARGE_TURBINE_ROTOR.getStackForm();
        ItemStack mediumTurbineRotorStackForm = GAMetaItems.MEDIUM_TURBINE_ROTOR.getStackForm();
        ItemStack smallTurbineRotorStackForm = GAMetaItems.SMALL_TURBINE_ROTOR.getStackForm();

        TurbineRotorBehavior.getInstanceFor(smallTurbineRotorStackForm).setPartMaterial(smallTurbineRotorStackForm, material);
        TurbineRotorBehavior.getInstanceFor(mediumTurbineRotorStackForm).setPartMaterial(mediumTurbineRotorStackForm, material);
        TurbineRotorBehavior.getInstanceFor(largeTurbineRotorStackForm).setPartMaterial(largeTurbineRotorStackForm, material);
        TurbineRotorBehavior.getInstanceFor(hugeTurbineRotorStackForm).setPartMaterial(hugeTurbineRotorStackForm, material);

        RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                .input(OrePrefix.turbineBlade, material, 4)
                .input(OrePrefix.stickLong, Materials.Titanium)
                .outputs(smallTurbineRotorStackForm)
                .duration(200).EUt(400)
                .buildAndRegister();
        RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                .input(OrePrefix.turbineBlade, material, 8)
                .input(OrePrefix.stickLong, Materials.Titanium)
                .outputs(mediumTurbineRotorStackForm)
                .duration(400).EUt(800)
                .buildAndRegister();
        RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                .input(OrePrefix.turbineBlade, material, 16)
                .input(OrePrefix.stickLong, Materials.Titanium)
                .outputs(largeTurbineRotorStackForm)
                .duration(800).EUt(1600)
                .buildAndRegister();

        RecipeMaps.ASSEMBLER_RECIPES.recipeBuilder()
                .input(OrePrefix.turbineBlade, material, 32)
                .input(OrePrefix.stickLong, Materials.Titanium)
                .outputs(hugeTurbineRotorStackForm)
                .duration(1600).EUt(3200)
                .buildAndRegister();

        RecipeMaps.FORMING_PRESS_RECIPES.recipeBuilder()
                .input(OrePrefix.plateDense, material, 5)
                .input(OrePrefix.screw, material, 2)
                .outputs(OreDictUnifier.get(toolPrefix, material))
                .duration(20).EUt(256)
                .buildAndRegister();

    }

    public static void registerLargeChemicalRecipes() {
        RecipeMaps.CHEMICAL_RECIPES.getRecipeList().forEach(recipe ->
                GARecipeMaps.LARGE_CHEMICAL_RECIPES.recipeBuilder()
                        .EUt(recipe.getEUt())
                        .duration(recipe.getDuration())
                        .fluidInputs(recipe.getFluidInputs())
                        .inputsIngredients(recipe.getInputs())
                        .outputs(recipe.getOutputs())
                        .fluidOutputs(recipe.getFluidOutputs())
                        .buildAndRegister());
    }

    public static void registerLargeMixerRecipes() {
        RecipeMaps.MIXER_RECIPES.getRecipeList().forEach(recipe ->
                GARecipeMaps.LARGE_MIXER_RECIPES.recipeBuilder()
                        .EUt(recipe.getEUt())
                        .duration(recipe.getDuration())
                        .fluidInputs(recipe.getFluidInputs())
                        .inputsIngredients(recipe.getInputs())
                        .outputs(recipe.getOutputs())
                        .fluidOutputs(recipe.getFluidOutputs())
                        .buildAndRegister());
    }

    public static void registerLargeForgeHammerRecipes() {
        RecipeMaps.FORGE_HAMMER_RECIPES.getRecipeList().forEach(recipe ->
                GARecipeMaps.LARGE_FORGE_HAMMER.recipeBuilder()
                        .EUt(recipe.getEUt())
                        .duration(recipe.getDuration())
                        .fluidInputs(Lubricant.getFluid(2))
                        .inputsIngredients(recipe.getInputs())
                        .outputs(recipe.getOutputs())
                        .fluidOutputs(recipe.getFluidOutputs())
                        .buildAndRegister());
    }


    public static void registerAlloyBlastRecipes() {
        for (Material material : Material.MATERIAL_REGISTRY) {
            if (!(material instanceof IngotMaterial))
                continue;
            IngotMaterial ingotMaterial = (IngotMaterial) material;
            if (ingotMaterial.blastFurnaceTemperature == 0)
                continue;

            GARecipeMaps.LARGE_MIXER_RECIPES.getRecipeList().stream()
                    .filter(recipe -> recipe.getOutputs().size() == 1)
                    .filter(recipe -> recipe.getOutputs().get(0).isItemEqualIgnoreDurability(OreDictUnifier.get(OrePrefix.dust, ingotMaterial)))
                    .findFirst()
                    .ifPresent(recipe -> {
                        ItemStack itemStack = recipe.getOutputs().get(0);
                        IngotMaterial ingot = ((IngotMaterial) (OreDictUnifier.getUnificationEntry(itemStack).material));
                        int duration = Math.max(1, (int) (ingot.getAverageMass() * ingot.blastFurnaceTemperature / 50L));
                        GARecipeMaps.BLAST_ALLOY_RECIPES.recipeBuilder()
                                .duration(duration * 80 / 100)
                                .EUt(120 * itemStack.getCount())
                                .fluidInputs(recipe.getFluidInputs())
                                .inputsIngredients(recipe.getInputs())
                                .fluidOutputs(ingot.getFluid(itemStack.getCount() * GTValues.L)).buildAndRegister();

                    });
        }
    }

    public static void registerChemicalPlantRecipes() {
        RecipeMaps.BREWING_RECIPES.getRecipeList().forEach(recipe -> {
            FluidStack fluidInput = recipe.getFluidInputs().get(0).copy();
            fluidInput.amount *= 10;
            CountableIngredient itemInput = new CountableIngredient(recipe.getInputs().get(0).getIngredient(), recipe.getInputs().get(0).getCount() * 10);
            FluidStack fluidOutput = FermentationBase.getFluid(recipe.getFluidOutputs().get(0).amount * 10);

            GARecipeMaps.CHEMICAL_PLANT_RECIPES.recipeBuilder()
                    .EUt(recipe.getEUt() * 10)
                    .duration(recipe.getDuration() * 10)
                    .fluidInputs(fluidInput)
                    .inputsIngredients(Collections.singleton(itemInput))
                    .fluidOutputs(fluidOutput)
                    .buildAndRegister();
        });
    }
}
