package boblovespi.factoryautomation.common.block.fluid;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.pipe.PipeNetwork;
import boblovespi.factoryautomation.common.tileentity.pipe.TEPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

/**
 * Created by Willi on 10/6/2018.
 * TODO: optimize w/ manager object
 */
public class Pipe extends FABaseBlock implements EntityBlock
{
	private static final EnumProperty<Connection> WEST = EnumProperty.create("west", Connection.class);
	private static final EnumProperty<Connection> EAST = EnumProperty.create("east", Connection.class);
	private static final EnumProperty<Connection> SOUTH = EnumProperty.create("south", Connection.class);
	private static final EnumProperty<Connection> NORTH = EnumProperty.create("north", Connection.class);
	private static final EnumProperty<Connection> DOWN = EnumProperty.create("down", Connection.class);
	private static final EnumProperty<Connection> UP = EnumProperty.create("up", Connection.class);

	public static final EnumProperty<Connection>[] CONNECTIONS = new EnumProperty[] {DOWN, UP, NORTH, SOUTH, WEST, EAST};

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
		if (te != null && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()).isPresent())
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

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		var state = defaultBlockState();
		var world = context.getLevel();
		var pos = context.getClickedPos();
		for (int i = 0; i < CONNECTIONS.length; i++)
		{
			state = state.setValue(CONNECTIONS[i], GetConnectionFor(world, pos, Direction.values()[i]));
		}
		return state;
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		if (world.isClientSide)
			return;
		if (state.is(oldState.getBlock()))
		{
			if (world.getBlockEntity(pos) instanceof TEPipe te)
			{
				for (Direction dir : Direction.values())
				{
					if (state.getValue(CONNECTIONS[dir.ordinal()]) == Connection.CONNECTOR)
						te.AddIONode(dir);
				}
			}
			return;
		}
		PipeNetwork net = null;
		PipeNetwork.Adjacencies adj = PipeNetwork.NONE;
		for (Direction dir : Direction.values())
		{
			if (state.getValue(CONNECTIONS[dir.ordinal()]) == Connection.NONE)
				continue;
			var te = world.getBlockEntity(pos.relative(dir));
			if (te instanceof TEPipe pipe)
			{
				adj = adj.With(dir, true);
				if (net == null)
					net = pipe.GetPipeNetwork();
				else
					net.Join(pipe.GetPipeNetwork(), world);
			}
		}
		if (net == null)
			net = new PipeNetwork(pos);
		if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TEPipe te)
		{
			te.SetPipeNetwork(net, adj);
			for (Direction dir : Direction.values())
			{
				if (state.getValue(CONNECTIONS[dir.ordinal()]) == Connection.CONNECTOR)
					te.AddIONode(dir);
			}
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		if (!state.is(oldState.getBlock()) && world.getBlockEntity(pos) instanceof TEPipe te && te.GetPipeNetwork() != null)
			te.GetPipeNetwork().Split(pos, world);
		super.onRemove(state, world, pos, oldState, isMoving);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
	{
		if (!world.isClientSide)
			System.out.println(((TEPipe) world.getBlockEntity(pos)).GetPipeNetwork().name);
		return InteractionResult.SUCCESS;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TEPipe(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		if (state.getValues().entrySet().stream().anyMatch(n -> n.getValue() == Connection.CONNECTOR))
			return ITickable::tickTE;
		return null;
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
