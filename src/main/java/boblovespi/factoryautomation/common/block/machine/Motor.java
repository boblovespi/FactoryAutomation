package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.api.energy.electricity.IEnergyBlock;
import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMotor;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

/**
 * Created by Willi on 3/19/2018.
 */
public class Motor extends FABaseBlock implements IEnergyBlock
{
	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public Motor()
	{
		super("motor", false, Properties.create(Material.IRON).hardnessAndResistance(3, 5).harvestLevel(0)
										.harvestTool(ToolType.PICKAXE),
				new Item.Properties().group(FAItemGroups.electrical));
		setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH));
		TileEntityHandler.tiles.add(TEMotor.class);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader reader)
	{
		return new TEMotor();
	}

	/**
	 * Checks whether or not a cable can connect to the given side and state
	 *
	 * @param state The state of the machine
	 * @param side  The side power is being connected to
	 * @param world The world access
	 * @param pos   The position of the block
	 * @return Whether or not a cable can attach to the given side and state
	 */
	@Override
	public boolean CanConnectCable(BlockState state, Direction side, IBlockReader world, BlockPos pos)
	{
		return side == state.get(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
}
