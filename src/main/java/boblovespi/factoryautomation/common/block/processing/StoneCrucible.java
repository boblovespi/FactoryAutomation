package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.processing.TEStoneCrucible;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/22/2018.
 */
public class StoneCrucible extends FABaseBlock
{
	public static final PropertyBool MULTIBLOCK_COMPLETE = PropertyBool.create("multiblock_complete");
	public static final PropertyEnum<EnumFacing> FACING = BlockHorizontal.FACING;
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(2 / 16d, 0, 2 / 16d, 14 / 16d, 1, 14 / 16d);

	public StoneCrucible()
	{
		super(Material.ROCK, "stone_crucible", FACreativeTabs.metallurgy);
		setDefaultState(getStateFromMeta(0));
		TileEntityHandler.tiles.add(TEStoneCrucible.class);
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BOUNDING_BOX;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TEStoneCrucible();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, MULTIBLOCK_COMPLETE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3))
								.withProperty(MULTIBLOCK_COMPLETE, (meta & 4) == 4);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex() | (state.getValue(MULTIBLOCK_COMPLETE) ? 4 : 0);
	}
}
