package boblovespi.factoryautomation.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

import net.minecraft.world.item.Item.Properties;

public class MusicDisc extends RecordItem implements FAItem
{
	private final String name;

	public MusicDisc(String name, int comparatorValue, Supplier<SoundEvent> soundSupplier, Properties builder)
	{
		super(comparatorValue, soundSupplier, builder.stacksTo(1).rarity(Rarity.RARE));
		this.name = name;
		setRegistryName(RegistryName());
		FAItems.items.add(this);
	}

	@Override
	public String UnlocalizedName()
	{
		return name;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return UnlocalizedName();
	}

	@Override
	public Item ToItem()
	{
		return this;
	}
}
