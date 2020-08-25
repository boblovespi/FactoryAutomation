package boblovespi.factoryautomation.common.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

import java.util.function.Consumer;

/**
 * Created by Willi on 4/12/2017.
 * the default interface for all factory automation blocks.  every block MUST implement this
 */
public interface FABlock extends IItemProvider
{
	String UnlocalizedName();

	default String RegistryName()
	{
		return UnlocalizedName();
	}

	default String GetMetaFilePath(int meta)
	{
		return RegistryName();
	}

	Block ToBlock();

	default boolean IsItemBlock()
	{
		return true;
	}

	default FABlock Init(Consumer<Block> apply)
	{
		apply.accept(this.ToBlock());
		return this;
	}

	@Override
	default Item asItem()
	{
		return ToBlock().asItem();
	}
}
