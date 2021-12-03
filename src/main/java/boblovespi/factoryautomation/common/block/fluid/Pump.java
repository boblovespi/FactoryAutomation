package boblovespi.factoryautomation.common.block.fluid;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TEPump;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/9/2018.
 */
public class Pump extends FABaseBlock
{
	public static final DirectionProperty FACING = DirectionalBlock.FACING;

	public Pump(String name)
	{
		super(Material.METAL, name, CreativeModeTab.TAB_DECORATIONS);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
		TileEntityHandler.tiles.add(TEPump.class);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter level)
	{
		return new TEPump();
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getClickedFace());
	}
}
