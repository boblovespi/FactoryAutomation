package boblovespi.factoryautomation.common.config;

import boblovespi.factoryautomation.common.util.Pair;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Willi on 5/2/2018.
 */
@Mod.EventBusSubscriber
// @LangKey("config.main")
// @Config(modid = MODID)
public class ConfigFields
{
	// @Name("tool mining levels")
	// @Comment("Category containing all configurations for tool mining level tweaks")
	// @LangKey("config.tool_mining_level")
	public static ToolMiningLevel toolMiningLevelCat = new ToolMiningLevel();
	// @Name("block mining levels")
	// @Comment("Category containing all configurations for block mining level tweaks")
	// @LangKey("config.block_mining_level")
	public static BlockMiningLevel blockMiningLevelCat = new BlockMiningLevel();
	public static PollutionCat pollutionCat = new PollutionCat();
	// @Ignore
	private static List<Pair<Method, SyncOnConfigChange.Priority>> methods = new ArrayList<>(10);

	public static void AddClass(Class c)
	{
		for (Method method : c.getMethods())
		{
			System.out.println("method = " + method.getName());
			if (Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())
					&& method.getParameterCount() == 0 && method.isAnnotationPresent(SyncOnConfigChange.class))
			{
				methods.add(new Pair<>(method, method.getAnnotation(SyncOnConfigChange.class).priority()));
			}
		}
		methods.sort(Comparator.comparing(Pair::getValue));
	}

	//	@SubscribeEvent
	//	public static void OnConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	//	{
	//		if (event.getModID().equals(MODID))
	//		{
	//			ConfigManager.sync(MODID, Type.INSTANCE);
	//			methods.forEach(n ->
	//			{
	//				try
	//				{
	//					n.getKey().invoke(null);
	//				} catch (Exception e)
	//				{
	//					e.printStackTrace();
	//				}
	//			});
	//		}
	//	}

	public static class ToolMiningLevel
	{
		// @LangKey("config.tool_mining_level.diamond")
		// @RangeInt(min = 0, max = 20)
		public int diamond = 5;
		// @LangKey("config.tool_mining_level.iron")
		// @RangeInt(min = 0, max = 20)
		public int iron = 3;
		// @LangKey("config.tool_mining_level.steel")
		// @RangeInt(min = 0, max = 20)
		public int steel = 5;
		// @LangKey("config.tool_mining_level.gold")
		// @RangeInt(min = 0, max = 20)
		public int gold = 2;
		// @LangKey("config.tool_mining_level.copper")
		// @RangeInt(min = 0, max = 20)
		public int copper = 2;
		// @LangKey("config.tool_mining_level.bronze")
		// @RangeInt(min = 0, max = 20)
		public int bronze = 4;
		// @LangKey("config.tool_mining_level.stone")
		// @RangeInt(min = 0, max = 20)
		public int stone = 1;
		// @LangKey("config.tool_mining_level.wood")
		// @RangeInt(min = 0, max = 20)
		public int wood = 0;
		public int netherite = 6;
	}

	public static class BlockMiningLevel
	{
		// @LangKey("config.block_mining_level.diamond_ore")
		// @RangeInt(min = 0, max = 20)
		public int diamondOre = 4;
		// @LangKey("config.block_mining_level.iron_ore")
		// @RangeInt(min = 0, max = 20)
		public int ironOre = 3;
		// @LangKey("config.block_mining_level.redstone_ore")
		// @RangeInt(min = 0, max = 20)
		public int redstoneOre = 3;
		// @LangKey("config.block_mining_level.limonite_ore")
		// @RangeInt(min = 0, max = 20)
		public int limoniteOre = 2;
		// @LangKey("config.block_mining_level.gold_ore")
		// @RangeInt(min = 0, max = 20)
		public int goldOre = 3;
		// @LangKey("config.block_mining_level.lapis_ore")
		// @RangeInt(min = 0, max = 20)
		public int lapisOre = 3;
		// @LangKey("config.block_mining_level.obsidian")
		// @RangeInt(min = 0, max = 20)
		public int obsidian = 5;
	}

	public static class PollutionCat
	{
		// @RangeDouble(min = 0.1)
		public double spillover = 20;
	}
}
