package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.IStringSerializable;

import java.util.function.Consumer;

/**
 * Created by Willi on 12/23/2017.
 */
public class MultiTypeBlock<T extends Enum<T> & IMultiTypeEnum & IStringSerializable> extends Block implements FABlock
{
	protected final Class<T> blockTypes;
	private final String registeryName;

	public /*final*/ EnumProperty<T> TYPE;
	private String resourceFolder;
	private FABaseBlock[] blocks;

	public MultiTypeBlock(String registeryName, Class<T> types, String resourceFolder, Properties properties,
			Item.Properties itemProperties)
	{
		super(properties);
		this.registeryName = registeryName;
		blockTypes = types;

		this.resourceFolder = resourceFolder;
		TYPE = EnumProperty.create("type", types);

		blocks = new FABaseBlock[blockTypes.getEnumConstants().length];

		for (int i = 0; i < blocks.length; i++)
		{
			blocks[i] = new FABaseBlock(registeryName + "_" + blockTypes.getEnumConstants()[i].getSerializedName(), false,
					properties, itemProperties)
			{
				@Override
				public String GetMetaFilePath(int meta)
				{
					String folder = (resourceFolder == null || resourceFolder.isEmpty()) ? "" : resourceFolder + "/";
					return folder + RegistryName();
				}
			};
		}

		//		registerDefaultState(blockState.getBaseState().withProperty(TYPE, blockTypes.getEnumConstants()[0]));
		//		setUnlocalizedName(UnlocalizedName());
		//		setRegistryName(RegistryName());
		//		FABlocks.blocks.add(this);
	}

	@Override
	public String UnlocalizedName()
	{
		return registeryName;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		String folder = (resourceFolder == null || resourceFolder.isEmpty()) ? "" : resourceFolder + "/";

		T[] types = blockTypes.getEnumConstants();

		for (int i = 0; i < types.length; i++)
			if (meta == i)
				return folder + UnlocalizedName() + "_" + types[i].getSerializedName();

		return folder + UnlocalizedName() + "_" + types[0].getSerializedName();
	}

	@Override
	public Block ToBlock() throws UnsupportedOperationException
	{
		return this;
	}

	public FABaseBlock GetBlock(T type)
	{
		return blocks[type.GetId()];
	}

	public MultiTypeBlock<T> Init(Consumer<Block> apply)
	{
		for (Block block : blocks)
		{
			apply.accept(block);
		}
		return this;
	}
}
