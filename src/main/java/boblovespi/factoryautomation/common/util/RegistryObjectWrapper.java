package boblovespi.factoryautomation.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegisterEvent;

import java.util.function.Function;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public record RegistryObjectWrapper<T>(ResourceLocation name, T obj)
{
	public static RegistryObjectWrapper<Block> Block(String name, Block block)
	{
		return new RegistryObjectWrapper<>(new ResourceLocation(MODID, name), block);
	}

	public static RegistryObjectWrapper<Item> Item(String name, Item item)
	{
		return new RegistryObjectWrapper<>(new ResourceLocation(MODID, name), item);
	}

	public static <T> RegistryObjectWrapper<T> Of(String name, T obj)
	{
		return new RegistryObjectWrapper<>(new ResourceLocation(MODID, name), obj);
	}

	public static <T> RegistryObjectWrapper<T> Of(ResourceLocation loc, T obj)
	{
		return new RegistryObjectWrapper<>(loc, obj);
	}

	public static <T> RegistryObjectWrapper<T> Of(String name, Function<String, T> constructor)
	{
		return Of(name, constructor.apply(name));
	}

	public void Register(RegisterEvent.RegisterHelper<T> register)
	{
		register.register(name, obj);
	}
}
