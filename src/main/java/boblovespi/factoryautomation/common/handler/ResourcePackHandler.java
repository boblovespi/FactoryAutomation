package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.util.Log;
import com.google.common.collect.Lists;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
	public static void OnServerStart(ServerAboutToStartEvent event)
	{
		event.getServer().getPackRepository().addPackFinder(new FAOverridePackFinder());
		event.getServer().getPackRepository().reload();
		List<String> ids = Lists.newArrayList(event.getServer().getPackRepository().getSelectedIds());
		ids.add("zfa_override_pack");
		event.getServer().getPackRepository().setSelected(ids);
		event.getServer().reloadResources(event.getServer().getPackRepository().getSelectedIds());
	}

	public static class FAOverridePackFinder implements RepositorySource
	{
		@Override
		public void loadPacks(Consumer<Pack> consumer, Pack.PackConstructor packInfoFactory) {
			// Thanks Dark Roleplay for showing off how to add more datapacks for each mod
			Pack pack = Pack.create("zfa_override_pack", false, () -> new FAOverridePack(Collections.singleton("minecraft")),
					packInfoFactory, Pack.Position.TOP, PackSource.DEFAULT);
			if (pack != null)
				consumer.accept(pack);
		}
	}

	public static class FAOverridePack extends AbstractPackResources
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
		public Collection<ResourceLocation> getResources(PackType type, String namespaceIn,
				String pathIn, Predicate<ResourceLocation> filterIn)
		{
			if (type == PackType.CLIENT_RESOURCES)
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
					collectResources(all, namespaceIn, path, pathIn, filterIn);
				} else if ("file".equals(uri.getScheme()))
				{
					URL url1 = new URL(url.toString().substring(0, url.toString().length() - "pack.mcmeta".length()));
					Path path = Paths.get(url1.toURI());
					collectResources(all, namespaceIn, path, pathIn, filterIn);
				}
			} catch (IOException | URISyntaxException exception)
			{
				//noinspection SpellCheckingInspection
				Log.LogError("AAAAAAAAAAAAAAAAAAAAAAA EVERYTHING IS BROKEN");
				Log.LogError(exception.getMessage());
				exception.printStackTrace();
				return Collections.emptyList();
			}
			return all;
		}

		@Override
		public Set<String> getNamespaces(PackType type)
		{
			return resourceNamespaces;
		}

		@Override
		public void close() {

		}

		private void collectResources(Collection<ResourceLocation> locations, String namespaceIn,
				Path pathIn, String pathNameIn, Predicate<ResourceLocation> filterIn) throws IOException
		{
			Path path = pathIn.resolve(namespaceIn);

			try (Stream<Path> stream = Files.walk(path.resolve(pathNameIn)))
			{
				stream.filter((n) -> !n.endsWith(".mcmeta") && Files.isRegularFile(n)).map((n) -> new ResourceLocation(namespaceIn,
						path.relativize(n).toString().replaceAll("\\\\", "/"))).filter(filterIn).forEach(locations::add);
			}
		}
	}
}
