package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;

import java.util.function.Supplier;

public class MusicDisc extends RecordItem
{
	public MusicDisc(String name, int comparatorValue, Supplier<SoundEvent> soundSupplier, Properties builder, int length)
	{
		super(comparatorValue, soundSupplier, builder.stacksTo(1).rarity(Rarity.RARE), length);
		FAItems.items.add(RegistryObjectWrapper.Item(name, this));
	}
}
