package boblovespi.factoryautomation.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * Created by Willi on 11/12/2017.
 */
public class FAFuel extends FABaseItem
{
	private int burnTime;

	public FAFuel(String unlocalizedName, CreativeTabs ct, int itemBurnTime)
	{
		super(unlocalizedName, ct);
		this.burnTime = itemBurnTime;
	}

	@Override
	public int getItemBurnTime(ItemStack itemStack)
	{
		return burnTime;
	}
}
