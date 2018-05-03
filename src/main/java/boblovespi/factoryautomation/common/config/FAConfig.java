package boblovespi.factoryautomation.common.config;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 11/10/2017.
 */
// @Config(modid = MODID)
public class FAConfig
{
	@Config.Ignore
	public static final String CATEGORY_NAME_TEST = "test";
	@Config.Ignore
	public static Configuration config;
	@Config.Ignore
	public static double testScalar;

	// TODO: figure out
	@Config.RangeInt(min = 0, max = 10)
	public static int woodHarvestLevel;

	public static void PreInit()
	{
		File configFile = new File(Loader.instance().getConfigDir(), "FactoryAutomation.cfg");
		config = new Configuration(configFile);
		SyncFromFiles();
	}

	public static Configuration GetConfig()
	{
		return config;
	}

	public static void ClientPreInit()
	{
		MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
	}

	public static void SyncFromFiles()
	{
		SyncConfig(true, true);
	}

	public static void SyncFromGui()
	{
		SyncConfig(false, true);
	}

	public static void SyncFromFields()
	{
		SyncConfig(false, false);
	}

	private static void SyncConfig(boolean loadFromFile, boolean readFieldsFromConfig)
	{
		if (loadFromFile)
		{
			config.load();
		}

		Property propertyTestScalar = config.get(CATEGORY_NAME_TEST, "test_scalar", 3.14);
		propertyTestScalar.setLanguageKey("gui.config" + CATEGORY_NAME_TEST + "test_scalar");
		propertyTestScalar.setComment(I18n.format("gui.config." + CATEGORY_NAME_TEST + ".test_scalar.comment"));
		propertyTestScalar.setMinValue(0);
		propertyTestScalar.setMaxValue(1000);

		List<String> order = Collections.singletonList(propertyTestScalar.getName());

		config.setCategoryPropertyOrder(CATEGORY_NAME_TEST, order);
		if (readFieldsFromConfig)
		{
			testScalar = propertyTestScalar.getDouble();
		}

		propertyTestScalar.set(testScalar);
		if (config.hasChanged())
			config.save();
	}

	public static class ConfigEventHandler
	{
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event)
		{
			if (Objects.equals(event.getModID(), MODID))
			{
				SyncFromGui();
			}
		}
	}
}
