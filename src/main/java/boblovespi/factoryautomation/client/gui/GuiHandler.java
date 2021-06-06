package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.common.container.*;
import boblovespi.factoryautomation.common.container.workbench.ContainerIronWorkbench;
import boblovespi.factoryautomation.common.container.workbench.ContainerStoneWorkbench;
import net.minecraft.client.gui.ScreenManager;

/**
 * Created by Willi on 11/12/2017.
 * gui handler
 */
// TODO: move to proper location
public class GuiHandler
{
	public static void registerGuis()
	{
		ScreenManager.registerFactory(ContainerBasicCircuitCreator.TYPE, GuiBasicCircuitCreator::new);
		ScreenManager.registerFactory(ContainerBlastFurnace.TYPE, GuiBlastFurnace::new);
		ScreenManager.registerFactory(ContainerBrickFoundry.TYPE, GuiBrickFoundry::new);
		ScreenManager.registerFactory(ContainerSolidFueledFirebox.TYPE, GuiSolidFueledFirebox::new);
		ScreenManager.registerFactory(ContainerSteelmakingFurnace.TYPE, GuiSteelmakingFurnace::new);
		ScreenManager.registerFactory(ContainerStoneCastingVessel.TYPE, GuiStoneCastingVessel::new);
		ScreenManager.registerFactory(ContainerStoneFoundry.TYPE, GuiStoneFoundry::new);
		ScreenManager.registerFactory(ContainerStoneWorkbench.TYPE, GuiWorkbench::new);
		ScreenManager.registerFactory(ContainerIronWorkbench.TYPE, GuiWorkbench::new);
	}
}
