package boblovespi.factoryautomation.common.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

/**
 * Created by Willi on 4/12/2017.
 * the default interface for all factory automation blocks.  every block MUST implement this
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface FABlock extends IItemProvider
{
	String unlocalizedName();

	default String registryName()
	{
		return unlocalizedName();
	}

	default String getMetaFilePath(int meta)
	{
		return registryName();
	}

	Block toBlock();

	default boolean isItemBlock()
	{
		return true;
	}

	default FABlock init(Consumer<Block> apply)
	{
		apply.accept(this.toBlock());
		return this;
	}

	@Override
	default Item asItem()
	{
		return toBlock().asItem();
	}
}
