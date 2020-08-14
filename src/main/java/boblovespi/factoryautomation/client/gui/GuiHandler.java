package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.common.container.ContainerBasicCircuitCreator;
import boblovespi.factoryautomation.common.container.ContainerBlastFurnace;
import net.minecraft.client.gui.ScreenManager;

/**
 * Created by Willi on 11/12/2017.
 * gui handler
 */
// TODO: move to proper location
public class GuiHandler
{
	public static void RegisterGuis()
	{
		ScreenManager.registerFactory(ContainerBasicCircuitCreator.TYPE, GuiBasicCircuitCreator::new);
		ScreenManager.registerFactory(ContainerBlastFurnace.TYPE, GuiBlastFurnace::new);
	}
}
