package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 12/1/2018.
 */
@Mod.EventBusSubscriber(modid = MODID)
public class TooltipHandler
{
	private static Map<Item, Component> tooltips;

	@SubscribeEvent
	public static void AddTooltip(ItemTooltipEvent event)
	{
		if (tooltips == null)
			return;
		ItemStack stack = event.getItemStack();
		Component tooltip = tooltips.get(stack.getItem());
		if (tooltip != null)
			event.getToolTip().add(tooltip);
	}

	public static void RegisterTooltips()
	{
		tooltips = new HashMap<>();
		String path = "/assets/factoryautomation/data/tooltips.txt";
		try (BufferedReader r = new BufferedReader(
				new InputStreamReader(Objects.requireNonNull(FactoryAutomation.class.getResourceAsStream(path)))))
		{
			String s;
			int split;
			while ((s = r.readLine()) != null)
			{
				if (s.startsWith("#"))
					continue;
				split = s.indexOf('=');
				if (split == -1)
					continue;

				String keyString = s.substring(0, split);
				String info = s.substring(split + 1);

				String[] keys = keyString.split(";");

				for (String key : keys)
				{
					Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(key));
					tooltips.putIfAbsent(item, new TextComponent("\u00A77" + info));
				}
			}

		} catch (Exception e)
		{
			Log.LogError("Tooltip file is malformed! " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
}
