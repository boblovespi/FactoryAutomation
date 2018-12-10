package boblovespi.factoryautomation.common.block.fluid;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TEPipe;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

/**
 * Created by Willi on 10/6/2018.
 */
public class Pipe extends FABaseBlock implements ITileEntityProvider
{
	private static final PropertyEnum<Connection> WEST = PropertyEnum.create("west", Connection.class);
	private static final PropertyEnum<Connection> EAST = PropertyEnum.create("east", Connection.class);
	private static final PropertyEnum<Connection> SOUTH = PropertyEnum.create("south", Connection.class);
	private static final PropertyEnum<Connection> NORTH = PropertyEnum.create("north", Connection.class);
	private static final PropertyEnum<Connection> DOWN = PropertyEnum.create("down", Connection.class);
	private static final PropertyEnum<Connection> UP = PropertyEnum.create("up", Connection.class);

	public static final PropertyEnum[] CONNECTIONS = new PropertyEnum[] { DOWN, UP, NORTH, SOUTH, WEST, EAST };

	public Pipe(String name)
	{
		super(Material.IRON, name, CreativeTabs.DECORATIONS);
		setDefaultState(getDefaultState().withProperty(UP, Connection.NONE).withProperty(DOWN, Connection.NONE)
										 .withProperty(NORTH, Connection.NONE).withProperty(SOUTH, Connection.NONE)
										 .withProperty(EAST, Connection.NONE).withProperty(WEST, Connection.NONE));
		TileEntityHandler.tiles.add(TEPipe.class);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, UP, DOWN, NORTH, SOUTH, EAST, WEST);
	}

	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return getDefaultState().withProperty(UP, GetConnectionFor(world, pos, EnumFacing.UP))
								.withProperty(DOWN, GetConnectionFor(world, pos, EnumFacing.DOWN))
								.withProperty(NORTH, GetConnectionFor(world, pos, EnumFacing.NORTH))
								.withProperty(SOUTH, GetConnectionFor(world, pos, EnumFacing.SOUTH))
								.withProperty(EAST, GetConnectionFor(world, pos, EnumFacing.EAST))
								.withProperty(WEST, GetConnectionFor(world, pos, EnumFacing.WEST));
	}

	private Connection GetConnectionFor(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		pos = pos.offset(side);
		if (world.getBlockState(pos).getBlock() instanceof Pipe || world.getBlockState(pos).getBlock() instanceof Pump)
			return Connection.JOIN;

		TileEntity te = world.getTileEntity(pos);
		if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()))
			return Connection.CONNECTOR;
		return Connection.NONE;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEPipe();
	}

	public enum Connection implements IStringSerializable
	{
		NONE("none"), CONNECTOR("connector"), JOIN("join");

		private String name;

		Connection(String name)
		{
			this.name = name;
		}

		@Override
		public String getName()
		{
			return name;
		}
	}
}
