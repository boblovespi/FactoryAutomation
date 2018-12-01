package boblovespi.factoryautomation.common.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Willi on 12/1/2018.
 */
@Mod.EventBusSubscriber
public class TooltipHandler
{
	private static Map<ItemInfo, String> tooltips;

	@SubscribeEvent
	public static void AddTooltip(ItemTooltipEvent event)
	{
		if (tooltips == null)
			return;
		ItemStack stack = event.getItemStack();
		ItemInfo item = new ItemInfo(stack.getItem(), stack.getItemDamage());
		String tooltip = tooltips.get(item);
		if (tooltip != null)
			event.getToolTip().add(tooltip);
	}

	public static void RegisterTooltips()
	{
		tooltips = new HashMap<>();
		ModContainer mod = Loader.instance().activeModContainer();
		if (mod == null)
			return;
		File source = mod.getSource();

		Path root = null;
		String base = "assets/factoryautomation/data";
		if (source.isFile())
		{
			try
			{
				FileSystem fs = FileSystems.newFileSystem(source.toPath(), null);
				root = fs.getPath("/" + base);
			} catch (IOException e)
			{
				FMLLog.log.error("Error loading FileSystem from jar: ", e);
				return;
			}
		} else if (source.isDirectory())
		{
			root = source.toPath().resolve(base);
		}

		if (root == null || !Files.exists(root))
			return;

		Path fPath = root.resolve("tooltips.txt");

		if (Files.exists(fPath))
		{
			try (BufferedReader r = Files.newBufferedReader(fPath))
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
						String[] parts = key.split("\\.");
						if (parts.length < 2)
							continue;
						Item item = Item.getByNameOrId(parts[0] + ":" + parts[1]);
						int meta = 0;

						if (parts.length > 2)
							meta = Integer.parseInt(parts[2]);

						tooltips.putIfAbsent(new ItemInfo(item, meta), "§7" + info);
					}
				}

			} catch (Exception e)
			{
				Log.LogError("Tooltip file is malformed! " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}
}
