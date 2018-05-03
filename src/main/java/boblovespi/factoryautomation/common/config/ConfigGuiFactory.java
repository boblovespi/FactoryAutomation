package boblovespi.factoryautomation.common.config;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Willi on 11/10/2017.
 */

@SuppressWarnings("unused")
public class ConfigGuiFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{

	}

	@Override
	public boolean hasConfigGui()
	{
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new FAConfigGui(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}

	public static class FAConfigGui extends GuiConfig
	{
		public FAConfigGui(GuiScreen parentScreen)
		{
			super(parentScreen, GetConfigElements(), FactoryAutomation.MODID, false, false,
					I18n.format("gui.config.main_title"));
		}

		private static List<IConfigElement> GetConfigElements()
		{
			List<IConfigElement> list = new ArrayList<>();
			list.add(new DummyConfigElement.DummyCategoryElement(I18n.format("gui.config.category.test"),
					"gui.config.category.test", CategoryEntryTest.class));
			return list;
		}

		public static class CategoryEntryTest extends GuiConfigEntries.CategoryEntry
		{

			public CategoryEntryTest(GuiConfig owningScreen, GuiConfigEntries owningEntryList,
					IConfigElement configElement)
			{
				super(owningScreen, owningEntryList, configElement);
			}

			@Override
			protected GuiScreen buildChildScreen()
			{
				Configuration config = FAConfig.GetConfig();
				ConfigElement categoryBLocks = new ConfigElement(config.getCategory(FAConfig.CATEGORY_NAME_TEST));
				List<IConfigElement> properties = categoryBLocks.getChildElements();
				String windowTitle = I18n.format("gui.config.category.test");
				return new GuiConfig(owningScreen, properties, owningScreen.modID,
						this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
						this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart, windowTitle);
			}
		}
	}
}

