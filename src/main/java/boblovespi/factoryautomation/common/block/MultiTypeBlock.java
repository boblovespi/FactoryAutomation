package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.function.Consumer;

/**
 * Created by Willi on 12/23/2017.
 */
public class MultiTypeBlock<T extends Enum<T> & IMultiTypeEnum & StringRepresentable>
{
	protected final Class<T> blockTypes;
	private final String registryName;

	public /*final*/ EnumProperty<T> TYPE;
	private String resourceFolder;
	private FABaseBlock[] blocks;

	public MultiTypeBlock(String registryName, Class<T> types, String resourceFolder,
						  BlockBehaviour.Properties properties, Item.Properties itemProperties)
	{
		this(registryName, types, resourceFolder, properties, itemProperties, 0);
	}

	public MultiTypeBlock(String registryName, Class<T> types, String resourceFolder,
						  BlockBehaviour.Properties properties, Item.Properties itemProperties, long ignore)
	{
		this.registryName = registryName;
		blockTypes = types;

		this.resourceFolder = resourceFolder;
		TYPE = EnumProperty.create("type", types);

		blocks = new FABaseBlock[blockTypes.getEnumConstants().length];

		for (int i = 0; i < blocks.length; i++)
		{
			if ((1L << i & ignore) != 0)
				continue;
			blocks[i] = new FABaseBlock(registryName + "_" + blockTypes.getEnumConstants()[i].getSerializedName(),
										false, properties, itemProperties);
		}

		//		registerDefaultState(blockState.getBaseState().withProperty(TYPE, blockTypes.getEnumConstants()[0]));
		//		setUnlocalizedName(UnlocalizedName());
		//		setRegistryName(RegistryName());
		//		FABlocks.blocks.add(this);
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
