package boblovespi.factoryautomation.common.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Created by Willi on 11/12/2017.
 */
public class FAFuel extends FABaseItem
{
	private final int burnTime;

	public FAFuel(String unlocalizedName, ItemGroup ct, int itemBurnTime)
	{
		super(unlocalizedName, ct);
		this.burnTime = itemBurnTime;
	}

	@Override
	public int getBurnTime(ItemStack itemStack)
	{
		return burnTime;
	}
}
