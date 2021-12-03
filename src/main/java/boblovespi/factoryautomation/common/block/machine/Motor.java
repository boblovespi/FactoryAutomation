package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.api.energy.electricity.IEnergyBlock;
import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMotor;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 3/19/2018.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Motor extends FABaseBlock implements IEnergyBlock
{
	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public Motor()
	{
		super("motor", false, Properties.of(Material.METAL).strength(3, 5).harvestLevel(0)
										.harvestTool(ToolType.PICKAXE),
				new Item.Properties().tab(FAItemGroups.electrical));
		registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
		TileEntityHandler.tiles.add(TEMotor.class);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter reader)
	{
		return new TEMotor();
	}

	/**
	 * Checks whether or not a cable can connect to the given side and state
	 *
	 * @param state The state of the machine
	 * @param side  The side power is being connected to
	 * @param level The level access
	 * @param pos   The position of the block
	 * @return Whether or not a cable can attach to the given side and state
	 */
	@Override
	public boolean CanConnectCable(BlockState state, Direction side, BlockGetter level, BlockPos pos)
	{
		return side == state.getValue(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter levelIn, BlockPos pos)
	{
		return Shapes.empty();
	}
}
