package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.api.recipe.BrickMakerRecipe;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class TEBrickMaker extends BlockEntity
{
	public String recipeL;
	public String recipeR;
	private ItemStackHandler items;

	public TEBrickMaker(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teBrickMaker.get(), pos, state);
		items = new ItemStackHandler(2);
		recipeL = "none";
		recipeR = "none";
	}

	public boolean CanDry()
	{
		return !("none".equals(recipeL) && "none".equals(recipeR));
	}

	public void Dry()
	{
		if ("none".equals(recipeL))
		{
			if ("none".equals(recipeR))
				return;
			var recipe = GetActive();
			if (recipe != null)
			{
				items.setStackInSlot(1, recipe.GetPrimaryItemOutputs().get(0).copy());
			}
			recipeR = "none";
		}
		else
		{
			var recipe = GetActive();
			if (recipe != null)
			{
				items.setStackInSlot(0, recipe.GetPrimaryItemOutputs().get(0).copy());
			}
			recipeL = "none";
		}
	}

	private BrickMakerRecipe GetActive()
	{
		if ("none".equals(recipeL))
		{
			if ("none".equals(recipeR))
				return null;
			return level.getRecipeManager().getAllRecipesFor(BrickMakerRecipe.TYPE).stream()
						.filter(r -> r.getId().toString().equals(recipeR)).findFirst().orElse(null);
		}
		return level.getRecipeManager().getAllRecipesFor(BrickMakerRecipe.TYPE).stream()
					.filter(r -> r.getId().toString().equals(recipeL)).findFirst().orElse(null);
	}
}
