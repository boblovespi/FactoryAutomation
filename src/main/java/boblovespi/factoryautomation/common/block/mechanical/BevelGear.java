package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEBevelGear;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 6/20/2019.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BevelGear extends FABaseBlock
{
	public static IntegerProperty LAYER = IntegerProperty.create("layer", 0, 2);
	public static DirectionProperty FACING = HorizontalBlock.FACING;

	public BevelGear()
	{
		super(Material.METAL, "bevel_gear", FAItemGroups.mechanical);
		TileEntityHandler.tiles.add(TEBevelGear.class);
	}

	public static Direction GetNegative(BlockState state)
	{
		if (state.getValue(LAYER) == 1)
			return state.getValue(FACING).getClockWise();
		else if (state.getValue(LAYER) == 0)
			return Direction.DOWN;
		else
			return Direction.UP;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(LAYER, FACING);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader level)
	{
		return new TEBevelGear();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState state = defaultBlockState();
		if (context.getHorizontalDirection() == Direction.UP)
			state = state.setValue(LAYER, 0);
		else if (context.getHorizontalDirection() == Direction.DOWN)
			state = state.setValue(LAYER, 1);
		else
			state = state
					.setValue(LAYER, context.getClickLocation().y > 2 / 3f ? 2 : context.getClickLocation().y > 1 / 3f ? 1 : 0);
		if (context.getHorizontalDirection().get2DDataValue() >= 0)
			state = state.setValue(FACING, context.getHorizontalDirection().getOpposite());
		else
			state = state.setValue(FACING, context.getHorizontalDirection().getOpposite());
		return state;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, IBlockReader levelIn, BlockPos pos)
	{
		return VoxelShapes.empty();
	}
}
