package boblovespi.factoryautomation.common.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 12/1/2018.
 */
@Mod.EventBusSubscriber(modid = MODID)
public class TooltipHandler
{
	private static Map<Item, ITextComponent> tooltips;

	@SubscribeEvent
	public static void AddTooltip(ItemTooltipEvent event)
	{
		if (tooltips == null)
			return;
		ItemStack stack = event.getItemStack();
		ITextComponent tooltip = tooltips.get(stack.getItem());
		if (tooltip != null)
			event.getToolTip().add(tooltip);
	}

	public static void RegisterTooltips()
	{
		tooltips = new HashMap<>();
		ModContainer mod = ModLoadingContext.get().getActiveContainer();
		if (mod == null)
			return;
		ModFile file = ((ModFileInfo) mod.getModInfo().getOwningFile()).getFile();
		Path source = file.getFilePath();

		Path root;
		String base = "assets/factoryautomation/data";

		// FileSystem fs = FileSystems.newFileSystem(source, FMLLoader.getLaunchClassLoader());
		root = source.resolve("/" + base);

		if (!Files.exists(root))
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
						Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryCreate(key));

						tooltips.putIfAbsent(item, new StringTextComponent("§7" + info));
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
