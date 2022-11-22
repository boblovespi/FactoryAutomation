package boblovespi.factoryautomation.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Created by Willi on 4/12/2017.
 * the default interface for all factory automation items.  every item MUST implement this
 */
@Deprecated
public interface FAItem extends ItemLike
{
	String UnlocalizedName();

	default String RegistryName()
	{
		return UnlocalizedName();
	}

	String GetMetaFilePath(int meta);

	Item ToItem();

	default FAItem Init(Consumer<Item> apply)
	{
		apply.accept(this.ToItem());
		return this;
	}

	@Override
	@Nonnull
	default Item asItem(){
		return ToItem();
	}
}
