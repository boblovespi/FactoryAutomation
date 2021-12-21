package boblovespi.factoryautomation.common.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Created by Willi on 11/12/2017.
 */
public class FAFuel extends FABaseItem
{
	private final int burnTime;

	public FAFuel(String unlocalizedName, CreativeModeTab ct, int itemBurnTime)
	{
		super(unlocalizedName, ct);
		this.burnTime = itemBurnTime;
	}

	@Override
	public int getBurnTime(ItemStack itemStack, RecipeType<?> type)
	{
		return burnTime;
	}
}
