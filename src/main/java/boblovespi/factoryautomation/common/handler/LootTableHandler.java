package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 7/2/2019.
 */
@Mod.EventBusSubscriber(modid = FactoryAutomation.MODID)
public class LootTableHandler
{
	public static void RegisterTables()
	{
		// LootTableList.register(new ResourceLocation(MODID, "entities/pig"));
	}

	@SubscribeEvent
	public static void OnLootTableLoad(LootTableLoadEvent event)
	{
		//		if (event.getName().toString().equals("minecraft:entities/pig"))
		//		{
		//			LootEntry[] tallowEntry = new LootEntry[] {
		//					new LootEntryTable(new ResourceLocation(MODID, "entities/pig"), 1, 1, new LootCondition[0],
		//							"tallow_entry") };
		//			LootPool tallowPool = new LootPool(
		//					tallowEntry, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0), "tallow_pool");
		//			event.getTable().addPool(tallowPool);
		//		}
	}
}
