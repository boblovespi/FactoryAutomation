package boblovespi.factoryautomation.common.block.fluid;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TEPipe;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
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

	public static final EnumProperty[] CONNECTIONS = new EnumProperty[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};

	public Pipe(String name)
	{
		super(Material.METAL, name, CreativeModeTab.TAB_DECORATIONS);
		registerDefaultState(stateDefinition.any().setValue(UP, Connection.NONE).setValue(DOWN, Connection.NONE)
									 .setValue(NORTH, Connection.NONE).setValue(SOUTH, Connection.NONE)
									 .setValue(EAST, Connection.NONE).setValue(WEST, Connection.NONE));
		TileEntityHandler.tiles.add(TEPipe.class);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(CONNECTIONS);
	}

	private Connection GetConnectionFor(BlockGetter world, BlockPos pos, Direction side)
	{
		pos = pos.relative(side);
		if (world.getBlockState(pos).getBlock() instanceof Pipe || world.getBlockState(pos).getBlock() instanceof Pump)
			return Connection.JOIN;

		BlockEntity te = world.getBlockEntity(pos);
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
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world,
								  BlockPos currentPos, BlockPos facingPos)
	{
		return state.setValue(CONNECTIONS[facing.ordinal()], GetConnectionFor(world, currentPos, facing));
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter level)
	{
		return new TEPipe();
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	public enum Connection implements StringRepresentable
	{
		NONE("none"), CONNECTOR("connector"), JOIN("join");

		private String name;

		Connection(String name)
		{
			this.name = name;
		}

		@Override
		public String getSerializedName()
		{
			return name;
		}
	}
}
