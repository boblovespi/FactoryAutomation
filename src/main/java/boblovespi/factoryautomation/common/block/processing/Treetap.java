package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.processing.TETreetap;
import boblovespi.factoryautomation.common.util.FAItemGroups;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 6/26/2018.
 */
public class Treetap extends FABaseBlock
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final VoxelShape BOUNDING_BOX = Block.box(0, 0, 0, 1, 1, 1);

	public Treetap()
	{
		super("treetap", false, Properties.of(Material.METAL).strength(1), new Item.Properties().tab(
				FAItemGroups.resources));
		TileEntityHandler.tiles.add(TETreetap.class);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter level)
	{
		return new TETreetap();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return BOUNDING_BOX;
	}

	/**
	 * Gets the {@link BlockState} to place
	 */
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
}
