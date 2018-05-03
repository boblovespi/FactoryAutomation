package boblovespi.factoryautomation.common.block;

import net.minecraft.block.Block;

import java.util.function.Consumer;

/**
 * Created by Willi on 4/12/2017.
 */
public interface FABlock
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
}
