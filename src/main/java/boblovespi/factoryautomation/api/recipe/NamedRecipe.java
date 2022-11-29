package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.resources.ResourceLocation;

public class NamedRecipe implements INamed
{
	private ResourceLocation name;

	protected NamedRecipe()
	{
		name = new ResourceLocation(FactoryAutomation.MODID, "unknown_recipe");
	}

	protected NamedRecipe(String name)
	{
		this.name = new ResourceLocation(FactoryAutomation.MODID, name);
	}

	protected NamedRecipe(ResourceLocation name)
	{
		this.name = name;
	}

	@Override
	public ResourceLocation getName()
	{
		return name;
	}

	protected void setName(ResourceLocation name)
	{
		this.name = name;
	}
}
