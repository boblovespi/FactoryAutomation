package boblovespi.factoryautomation.client.model;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Willi on 11/24/2017.
 */
@Deprecated
public class ObjModelLoader implements ICustomModelLoader
{
	private static Map<ResourceLocation, IModel> cache = new HashMap<>();

	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return modelLocation.getResourcePath().endsWith(".obj");
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		IModel model;
		if (cache.containsKey(modelLocation))
			model = cache.get(modelLocation);
		else
			model = OBJLoader.INSTANCE.loadModel(modelLocation);

		return model == null ? ModelLoaderRegistry.getMissingModel() : model;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		cache.clear();
	}
}
