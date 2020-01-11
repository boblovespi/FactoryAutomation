package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEBevelGear;
import boblovespi.factoryautomation.common.util.FAItemGroups;
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
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * Created by Willi on 6/20/2019.
 */
public class BevelGear extends FABaseBlock
{
	public static IntegerProperty LAYER = IntegerProperty.create("layer", 0, 2);
	public static DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	public BevelGear()
	{
		super(Material.IRON, "bevel_gear", FAItemGroups.mechanical);
		TileEntityHandler.tiles.add(TEBevelGear.class);
	}

	public static Direction GetNegative(BlockState state)
	{
		if (state.get(LAYER) == 1)
			return state.get(FACING).rotateY();
		else if (state.get(LAYER) == 0)
			return Direction.DOWN;
		else
			return Direction.UP;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
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
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TEBevelGear();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState state = getDefaultState();
		if (context.getFace() == Direction.UP)
			state = state.with(LAYER, 0);
		else if (context.getFace() == Direction.DOWN)
			state = state.with(LAYER, 1);
		else
			state = state
					.with(LAYER, context.getHitVec().getY() > 2 / 3f ? 2 : context.getHitVec().getY() > 1 / 3f ? 1 : 0);
		if (context.getFace().getHorizontalIndex() >= 0)
			state = state.with(FACING, context.getFace().getOpposite());
		else
			state = state.with(FACING, context.getPlacementHorizontalFacing().getOpposite());
		return state;
	}
}
