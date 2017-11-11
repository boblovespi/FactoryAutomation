package boblovespi.factoryautomation.common.config;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
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

/**
 * Created by Willi on 11/10/2017.
 */
public class Config
{
	public static final String CATEGORY_NAME_TEST = "test";
	public static Configuration config;
	public static double testScalar;

	public static void PreInit()
	{
		File configFile = new File(Loader.instance().getConfigDir(),
				"FactoryAutomation.cfg");
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

	private static void SyncConfig(boolean loadFromFile,
			boolean readFieldsFromConfig)
	{
		if (loadFromFile)
		{
			config.load();
		}

		Property propertyTestScalar = config
				.get(CATEGORY_NAME_TEST, "test_scalar", 3.14);
		propertyTestScalar.setLanguageKey(
				"gui.config" + CATEGORY_NAME_TEST + "test_scalar");
		propertyTestScalar.setComment(I18n.format(
				"gui.config." + CATEGORY_NAME_TEST + ".test_scalar.comment"));
		propertyTestScalar.setMinValue(0);
		propertyTestScalar.setMaxValue(1000);

		List<String> order = Collections
				.singletonList(propertyTestScalar.getName());

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
			if (Objects.equals(event.getModID(), FactoryAutomation.MODID))
			{
				SyncFromGui();
			}
		}
	}
}
