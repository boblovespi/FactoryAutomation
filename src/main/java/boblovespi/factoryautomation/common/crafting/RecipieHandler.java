package boblovespi.factoryautomation.common.crafting;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipieHandler
{

    public static void RegisterCraftingRecipes()
    {
        GameRegistry.addShapelessRecipe(null, null, new ItemStack(FABlocks.concrete.ToBlock()),Ingredient.fromItem(FAItems.slag.ToItem()), Ingredient.fromItem(Items.WATER_BUCKET));
        Log.getLogger().info("Registered Recipes");

    }
    public static void RegisterSmeltingRecipes()
    {
        Log.getLogger().info("Registered Smelting Recipes");

    }

}
