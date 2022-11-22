package boblovespi.factoryautomation.common.container;

import boblovespi.factoryautomation.common.container.workbench.ContainerIronWorkbench;
import boblovespi.factoryautomation.common.container.workbench.ContainerStoneWorkbench;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerManager
{
	@SubscribeEvent
	public static void RegisterTypes(RegisterEvent event)
	{
		event.register(ForgeRegistries.Keys.MENU_TYPES, n ->
		{
			n.register("basic_circuit_creator", ContainerBasicCircuitCreator.TYPE);
			n.register("blast_furnace", ContainerBlastFurnace.TYPE);
			n.register("brick_foundry", ContainerBrickFoundry.TYPE);
			n.register("solid_fueled_firebox", ContainerSolidFueledFirebox.TYPE);
			n.register("steelmaking_furnace", ContainerSteelmakingFurnace.TYPE);
			n.register("stone_casting_vessel", ContainerStoneCastingVessel.TYPE);
			n.register("stone_foundry", ContainerStoneFoundry.TYPE);
			n.register("iron_workbench", ContainerIronWorkbench.TYPE);
			n.register("stone_workbench", ContainerStoneWorkbench.TYPE);
			n.register("tumbling_barrel", ContainerTumblingBarrel.TYPE);
		});
	}
}
