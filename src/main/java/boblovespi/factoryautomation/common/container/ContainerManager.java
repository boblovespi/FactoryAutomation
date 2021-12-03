package boblovespi.factoryautomation.common.container;

import boblovespi.factoryautomation.common.container.workbench.ContainerIronWorkbench;
import boblovespi.factoryautomation.common.container.workbench.ContainerStoneWorkbench;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerManager
{
	@SubscribeEvent
	public static void RegisterTypes(RegistryEvent.Register<MenuType<?>> event)
	{
		event.getRegistry().register(ContainerBasicCircuitCreator.TYPE.setRegistryName(MODID, "basic_circuit_creator"));
		event.getRegistry().register(ContainerBlastFurnace.TYPE.setRegistryName(MODID, "blast_furnace"));
		event.getRegistry().register(ContainerBrickFoundry.TYPE.setRegistryName(MODID, "brick_foundry"));
		event.getRegistry().register(ContainerSolidFueledFirebox.TYPE.setRegistryName(MODID, "solid_fueled_firebox"));
		event.getRegistry().register(ContainerSteelmakingFurnace.TYPE.setRegistryName(MODID, "steelmaking_furnace"));
		event.getRegistry().register(ContainerStoneCastingVessel.TYPE.setRegistryName(MODID, "stone_casting_vessel"));
		event.getRegistry().register(ContainerStoneFoundry.TYPE.setRegistryName(MODID, "stone_foundry"));
		event.getRegistry().register(ContainerIronWorkbench.TYPE.setRegistryName(MODID, "iron_workbench"));
		event.getRegistry().register(ContainerStoneWorkbench.TYPE.setRegistryName(MODID, "stone_workbench"));
	}
}
