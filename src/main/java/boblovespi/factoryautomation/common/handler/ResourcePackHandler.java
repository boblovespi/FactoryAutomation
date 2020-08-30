package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class ResourcePackHandler
{
	@SubscribeEvent
	public static void OnServerStart(FMLServerAboutToStartEvent event)
	{
		event.getServer().getResourcePacks().addPackFinder(new FAOverridePackFinder());
	}

	public static class FAOverridePackFinder implements IPackFinder
	{
		@Override
		public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap,
				ResourcePackInfo.IFactory<T> packInfoFactory)
		{
			// Thanks Dark Roleplay for showing off how to add more datapacks for each mod
			T pack = ResourcePackInfo
					.createResourcePack("zfa_override_pack", false, () -> new FAOverridePack(Set.of("minecraft")),
							packInfoFactory, ResourcePackInfo.Priority.TOP);
			if (pack != null)
				nameToPackMap.put("zfa_override_pack", pack);
		}
	}

	public static class FAOverridePack extends ResourcePack
	{
		private final String root = "/data/factoryautomation/other_packs/zfa_override_pack/";
		private Set<String> resourceNamespaces;

		public FAOverridePack(Set<String> resourceNamespaces)
		{
			super(new File("zfa_override_pack"));
			this.resourceNamespaces = resourceNamespaces;
		}

		@Override
		protected InputStream getInputStream(String resourcePath) throws IOException
		{
			return FactoryAutomation.class.getResourceAsStream(root + resourcePath);
		}

		@Override
		protected boolean resourceExists(String resourcePath)
		{
			return FactoryAutomation.class.getResource(root + resourcePath) != null;
		}

		@Override
		public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String namespaceIn,
				String pathIn, int maxDepthIn, Predicate<String> filterIn)
		{
			if (type == ResourcePackType.CLIENT_RESOURCES)
				return Collections.emptyList();

			Collection<ResourceLocation> all = new ArrayList<>();

			URL url = FactoryAutomation.class.getResource(root + "pack.mcmeta");
			try
			{
				URI uri = url.toURI();
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
					Path path = filesystem.getPath("/" + type.getDirectoryName());
					collectResources(all, maxDepthIn, namespaceIn, path, pathIn, filterIn);
				} else if ("file".equals(uri.getScheme()))
				{
					URL url1 = new URL(url.toString().substring(0, url.toString().length() - "pack.mcmeta".length()));
					Path path = Paths.get(url1.toURI());
					collectResources(all, maxDepthIn, namespaceIn, path, pathIn, filterIn);
				}
			} catch (IOException | URISyntaxException exception)
			{
				Log.LogError("AAAAAAAAAAAAAAAAAAAAAAA EVERYTHING IS BROKEN");
				Log.LogError(exception.getMessage());
				exception.printStackTrace();
				return Collections.emptyList();
			}
			return all;
		}

		@Override
		public Set<String> getResourceNamespaces(ResourcePackType type)
		{
			return resourceNamespaces;
		}

		@Override
		public void close() throws IOException
		{

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
