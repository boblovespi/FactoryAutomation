package boblovespi.factoryautomation.common.tileentity.workbench;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by Willi on 4/8/2018.
 */
public abstract class TEWorkbench extends TileEntity
{
	protected IItemHandler inventory;

	public TEWorkbench(int size)
	{
		inventory = new ItemStackHandler(size * size + size * 2);
	}
}
