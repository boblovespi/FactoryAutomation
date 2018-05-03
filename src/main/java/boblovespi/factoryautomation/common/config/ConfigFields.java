package boblovespi.factoryautomation.common.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;
import static net.minecraftforge.common.config.Config.*;

/**
 * Created by Willi on 5/2/2018.
 */
@Mod.EventBusSubscriber
@LangKey("config.main")
@Config(modid = MODID)
public class ConfigFields
{
	@Name("mining levels")
	@Comment("Category containing all configurations for mining level tweaks")
	@LangKey("config.mining_level")
	public static MiningLevelCat miningLevelCat = new MiningLevelCat();

	@SubscribeEvent
	public void OnConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(MODID))
		{
			ConfigManager.sync(MODID, Type.INSTANCE);
		}
	}

	private static class MiningLevelCat
	{
		@LangKey("config.mining_level.diamond")
		@RangeInt(min = 0, max = 20)
		public int diamond = 5;
		@LangKey("config.mining_level.iron")
		@RangeInt(min = 0, max = 20)
		public int iron = 3;
		@LangKey("config.mining_level.steel")
		@RangeInt(min = 0, max = 20)
		public int steel = 5;
		@LangKey("config.mining_level.gold")
		@RangeInt(min = 0, max = 20)
		public int gold = 2;
		@LangKey("config.mining_level.copper")
		@RangeInt(min = 0, max = 20)
		public int copper = 2;
		@LangKey("config.mining_level.bronze")
		@RangeInt(min = 0, max = 20)
		public int bronze = 5;
		@LangKey("config.mining_level.stone")
		@RangeInt(min = 0, max = 20)
		public int stone = 1;
		@LangKey("config.mining_level.wood")
		@RangeInt(min = 0, max = 20)
		public int wood = 0;
	}
}
