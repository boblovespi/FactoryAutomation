package boblovespi.factoryautomation.common.handler;

import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by Willi on 2/11/2018.
 */

@Mod.EventBusSubscriber
@SuppressWarnings("unused")
public class AnvilRecipeHandler
{
	@SubscribeEvent
	public static void anvilUpdateEvent(AnvilUpdateEvent event)
	{
		//		System.out.println("Anvil Update Event Triggered!");
		//		if (event.getLeft().getItem().equals(FAItems.ingot.ToItem()))
		//		{
		//			event.setOutput(new ItemStack(FAItems.sheet.ToItem(), 1,
		//										  event.getLeft().getMetadata()));
		//			event.setCost(1);
		//			event.setMaterialCost(0);
		//		}
	}
}
