package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.util.patchouli.PatchouliHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModList;

/**
 * Created by Willi on 2/23/2019.
 */
public class ModCompatHandler
{
	public static void preInit()
	{

	}

	public static void init()
	{
		if (ModList.get().isLoaded("patchouli"))
			PatchouliHelper.RegisterMultiblocks();
	}

	public static void postInit()
	{

	}

	public static ItemStack getGuidebook()
	{
		if (ModList.get().isLoaded("patchouli"))
			return PatchouliHelper.GetGuidebook();
		return ItemStack.EMPTY;
	}
}
