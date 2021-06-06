package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.IStringSerializable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

/**
 * Created by Willi on 12/23/2017.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MultiTypeBlock<T extends Enum<T> & IMultiTypeEnum & IStringSerializable> extends Block implements FABlock
{
	protected final Class<T> blockTypes;
	private final String registeryName;

	public /*final*/ EnumProperty<T> TYPE;
	private final String resourceFolder;
	private final FABaseBlock[] blocks;

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
				public String getMetaFilePath(int meta)
				{
					String folder = (resourceFolder == null || resourceFolder.isEmpty()) ? "" : resourceFolder + "/";
					return folder + registryName();
				}
			};
		}

		//		registerDefaultState(blockState.getBaseState().withProperty(TYPE, blockTypes.getEnumConstants()[0]));
		//		setUnlocalizedName(UnlocalizedName());
		//		setRegistryName(RegistryName());
		//		FABlocks.blocks.add(this);
	}

	@Override
	public String unlocalizedName()
	{
		return registeryName;
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		String folder = (resourceFolder == null || resourceFolder.isEmpty()) ? "" : resourceFolder + "/";

		T[] types = blockTypes.getEnumConstants();

		for (int i = 0; i < types.length; i++)
			if (meta == i)
				return folder + unlocalizedName() + "_" + types[i].getSerializedName();

		return folder + unlocalizedName() + "_" + types[0].getSerializedName();
	}

	@Override
	public Block toBlock() throws UnsupportedOperationException
	{
		return this;
	}

	public FABaseBlock getBlock(T type)
	{
		return blocks[type.getId()];
	}

	public MultiTypeBlock<T> init(Consumer<Block> apply)
	{
		for (Block block : blocks)
		{
			apply.accept(block);
		}
		return this;
	}
}
