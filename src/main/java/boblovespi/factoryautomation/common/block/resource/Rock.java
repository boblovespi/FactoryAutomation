package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Willi on 12/26/2018.
 */
public class Rock extends FABaseBlock
{
	public static final PropertyEnum<Variants> VARIANTS = PropertyEnum.create("variants", Variants.class);
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(
			3 / 16d, 0, 3 / 16d, 13 / 16d, 5 / 16d, 13 / 16d);

	public Rock()
	{
		super(Materials.ROCKS, "rock", FACreativeTabs.resources);
		setDefaultState(getDefaultState().withProperty(VARIANTS, Variants.COBBLESTONE));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, VARIANTS);
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BOUNDING_BOX;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "resources/" + RegistryName();
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(VARIANTS, Variants.values()[meta]);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(VARIANTS).ordinal();
	}

	public enum Variants implements IStringSerializable
	{
		COBBLESTONE("cobblestone"),
		STONE("stone"),
		ANDESITE("andesite"),
		DIORITE("diorite"),
		GRANITE("granite"),
		SANDSTONE("sandstone"),
		MOSSY_COBBLESTONE("mossy_cobblestone");

		private final String name;

		Variants(String name)
		{
			this.name = name;
		}

		@Override
		public String getName()
		{
			return name;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
