package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.common.container.*;
import boblovespi.factoryautomation.common.container.workbench.ContainerIronWorkbench;
import boblovespi.factoryautomation.common.container.workbench.ContainerStoneWorkbench;
import net.minecraft.client.gui.screens.MenuScreens;

/**
 * Created by Willi on 11/12/2017.
 * gui handler
 */
// TODO: move to proper location
public class GuiHandler
{
	public static void RegisterGuis()
	{
		MenuScreens.register(ContainerBasicCircuitCreator.TYPE, GuiBasicCircuitCreator::new);
		MenuScreens.register(ContainerBlastFurnace.TYPE, GuiBlastFurnace::new);
		MenuScreens.register(ContainerBrickFoundry.TYPE, GuiBrickFoundry::new);
		MenuScreens.register(ContainerSolidFueledFirebox.TYPE, GuiSolidFueledFirebox::new);
		MenuScreens.register(ContainerSteelmakingFurnace.TYPE, GuiSteelmakingFurnace::new);
		MenuScreens.register(ContainerStoneCastingVessel.TYPE, GuiStoneCastingVessel::new);
		MenuScreens.register(ContainerStoneFoundry.TYPE, GuiStoneFoundry::new);
		MenuScreens.register(ContainerStoneWorkbench.TYPE, GuiWorkbench::new);
		MenuScreens.register(ContainerIronWorkbench.TYPE, GuiWorkbench::new);
		MenuScreens.register(ContainerTumblingBarrel.TYPE, GuiTumblingBarrel::new);
	}
}
