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
	public static void RegisterGuis()
	{
		ScreenManager.register(ContainerBasicCircuitCreator.TYPE, GuiBasicCircuitCreator::new);
		ScreenManager.register(ContainerBlastFurnace.TYPE, GuiBlastFurnace::new);
		ScreenManager.register(ContainerBrickFoundry.TYPE, GuiBrickFoundry::new);
		ScreenManager.register(ContainerSolidFueledFirebox.TYPE, GuiSolidFueledFirebox::new);
		ScreenManager.register(ContainerSteelmakingFurnace.TYPE, GuiSteelmakingFurnace::new);
		ScreenManager.register(ContainerStoneCastingVessel.TYPE, GuiStoneCastingVessel::new);
		ScreenManager.register(ContainerStoneFoundry.TYPE, GuiStoneFoundry::new);
		ScreenManager.register(ContainerStoneWorkbench.TYPE, GuiWorkbench::new);
		ScreenManager.register(ContainerIronWorkbench.TYPE, GuiWorkbench::new);
	}
}
