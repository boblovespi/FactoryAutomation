package boblovespi.factoryautomation.common.block.fluid;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TEPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

/**
 * Created by Willi on 10/6/2018.
 * TODO: optimize w/ manager object
 */
public class Pipe extends FABaseBlock
{
	private static final EnumProperty<Connection> WEST = EnumProperty.create("west", Connection.class);
	private static final EnumProperty<Connection> EAST = EnumProperty.create("east", Connection.class);
	private static final EnumProperty<Connection> SOUTH = EnumProperty.create("south", Connection.class);
	private static final EnumProperty<Connection> NORTH = EnumProperty.create("north", Connection.class);
	private static final EnumProperty<Connection> DOWN = EnumProperty.create("down", Connection.class);
	private static final EnumProperty<Connection> UP = EnumProperty.create("up", Connection.class);

	public static final EnumProperty[] CONNECTIONS = new EnumProperty[] { DOWN, UP, NORTH, SOUTH, WEST, EAST };

	public Pipe(String name)
	{
		super(Material.IRON, name, ItemGroup.DECORATIONS);
		setDefaultState(stateContainer.getBaseState().with(UP, Connection.NONE).with(DOWN, Connection.NONE)
									  .with(NORTH, Connection.NONE).with(SOUTH, Connection.NONE)
									  .with(EAST, Connection.NONE).with(WEST, Connection.NONE));
		TileEntityHandler.tiles.add(TEPipe.class);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(CONNECTIONS);
	}

	private Connection GetConnectionFor(IBlockReader world, BlockPos pos, Direction side)
	{
		pos = pos.offset(side);
		if (world.getBlockState(pos).getBlock() instanceof Pipe || world.getBlockState(pos).getBlock() instanceof Pump)
			return Connection.JOIN;

		TileEntity te = world.getTileEntity(pos);
		if (te != null && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite())
							.isPresent())
			return Connection.CONNECTOR;
		return Connection.NONE;
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific face passed in.
	 */
	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world,
			BlockPos currentPos, BlockPos facingPos)
	{
		return state.with(CONNECTIONS[facing.getIndex()], GetConnectionFor(world, currentPos, facing));
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TEPipe();
	}

	@Override
	public boolean hasTileEntity()
	{
		return true;
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
