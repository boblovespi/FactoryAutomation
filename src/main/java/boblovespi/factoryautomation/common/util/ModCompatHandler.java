package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.util.patchouli.PatchouliHelper;
import net.minecraftforge.fml.common.Loader;

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
		if (Loader.isModLoaded("patchouli"))
			PatchouliHelper.RegisterMultiblocks();
	}

	public static void PostInit()
	{

	}
}
