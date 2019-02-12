package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMillstone;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 2/12/2019.
 */
public class Millstone extends FABaseBlock
{
	public static final PropertyBool IS_TOP = PropertyBool.create("is_top");

	public Millstone()
	{
		super(Material.ROCK, "millstone", FACreativeTabs.mechanical);
		setHardness(2.5f);
		TileEntityHandler.tiles.add(TEMillstone.class);
		setDefaultState(getDefaultState().withProperty(IS_TOP, false));
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "mechanical/" + RegistryName();
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
		return new TEMillstone();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, IS_TOP);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
}
