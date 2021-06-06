package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.util.Log;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.resources.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(modid = MODID)
public class ResourcePackHandler
{
	@SubscribeEvent
	public static void OnServerStart(FMLServerAboutToStartEvent event)
	{
		event.getServer().getPackRepository().addPackFinder(new FAOverridePackFinder());
	}

	public static class FAOverridePackFinder implements IPackFinder
	{
		@Override
		public void loadPacks(Consumer<ResourcePackInfo> consumer, ResourcePackInfo.IFactory packInfoFactory) {
			// Thanks Dark Roleplay for showing off how to add more datapacks for each mod
			ResourcePackInfo pack = ResourcePackInfo.create("zfa_override_pack", false, () -> new FAOverridePack(Collections.singleton("minecraft")),
					packInfoFactory, ResourcePackInfo.Priority.TOP, IPackNameDecorator.DEFAULT);
			if (pack != null)
				consumer.accept(pack);
		}
	}

	public static class FAOverridePack extends ResourcePack
	{
		private final String root = "/data/factoryautomation/other_packs/zfa_override_pack/";
		private final Set<String> resourceNamespaces;

		public FAOverridePack(Set<String> resourceNamespaces)
		{
			super(new File("zfa_override_pack"));
			this.resourceNamespaces = resourceNamespaces;
		}

		@Override
		protected InputStream getResource(String resourcePath) {
			return Objects.requireNonNull(FactoryAutomation.class.getResourceAsStream(root + resourcePath));
		}

		@Override
		protected boolean hasResource(String resourcePath)
		{
			return FactoryAutomation.class.getResource(root + resourcePath) != null;
		}

		@Override
		public Collection<ResourceLocation> getResources(ResourcePackType type, String namespaceIn,
				String pathIn, int maxDepthIn, Predicate<String> filterIn)
		{
			if (type == ResourcePackType.CLIENT_RESOURCES)
				return Collections.emptyList();

			Collection<ResourceLocation> all = new ArrayList<>();

			URL url = FactoryAutomation.class.getResource(root + "pack.mcmeta");
			try
			{
				URI uri = Objects.requireNonNull(url).toURI();
				if ("jar".equals(uri.getScheme()))
				{
					FileSystem filesystem;
					try
					{
						filesystem = FileSystems.getFileSystem(uri);
					} catch (FileSystemNotFoundException var11)
					{
						filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
					}
					Path path = filesystem.getPath("/" + type.getDirectory());
					collectResources(all, maxDepthIn, namespaceIn, path, pathIn, filterIn);
				} else if ("file".equals(uri.getScheme()))
				{
					URL url1 = new URL(url.toString().substring(0, url.toString().length() - "pack.mcmeta".length()));
					Path path = Paths.get(url1.toURI());
					collectResources(all, maxDepthIn, namespaceIn, path, pathIn, filterIn);
				}
			} catch (IOException | URISyntaxException exception)
			{
				//noinspection SpellCheckingInspection
				Log.logError("AAAAAAAAAAAAAAAAAAAAAAA EVERYTHING IS BROKEN");
				Log.logError(exception.getMessage());
				exception.printStackTrace();
				return Collections.emptyList();
			}
			return all;
		}

		@Override
		public Set<String> getNamespaces(ResourcePackType type)
		{
			return resourceNamespaces;
		}

		@Override
		public void close() {

		}

		private void collectResources(Collection<ResourceLocation> locations, int maxDepthIn, String namespaceIn,
				Path pathIn, String pathNameIn, Predicate<String> filterIn) throws IOException
		{
			Path path = pathIn.resolve(namespaceIn);

			try (Stream<Path> stream = Files.walk(path.resolve(pathNameIn), maxDepthIn))
			{
				stream.filter((n) -> !n.endsWith(".mcmeta") && Files.isRegularFile(n) && filterIn
						.test(n.getFileName().toString())).map((n) -> new ResourceLocation(namespaceIn,
						path.relativize(n).toString().replaceAll("\\\\", "/"))).forEach(locations::add);
			}
		}
	}
}
