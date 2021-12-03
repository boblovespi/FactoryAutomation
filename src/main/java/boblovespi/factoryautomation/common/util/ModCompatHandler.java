package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.util.patchouli.PatchouliHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

/**
 * Created by Willi on 2/23/2019.
 */
public class ModCompatHandler
{
	public static void PreInit()
	{

	}

	public static void Init()
	{
		if (ModList.get().isLoaded("patchouli"))
			PatchouliHelper.RegisterMultiblocks();
	}

	public static void PostInit()
	{

	}

	public static ItemStack GetGuidebook()
	{
		if (ModList.get().isLoaded("patchouli"))
			return PatchouliHelper.GetGuidebook();
		return ItemStack.EMPTY;
	}
}
