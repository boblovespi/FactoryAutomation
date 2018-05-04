package boblovespi.factoryautomation.common.config;

import javafx.util.Pair;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
	@Ignore
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

	@SubscribeEvent
	public static void OnConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(MODID))
		{
			ConfigManager.sync(MODID, Type.INSTANCE);
			methods.forEach(n ->
			{
				try
				{
					n.getKey().invoke(null);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			});
		}
	}

	public static class MiningLevelCat
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
