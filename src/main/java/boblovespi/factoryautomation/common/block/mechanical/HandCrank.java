package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEHandCrank;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 9/3/2018.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HandCrank extends FABaseBlock
{
	public static final BooleanProperty INVERTED = BooleanProperty.create("inverted");
	private static final VoxelShape BOUNDING_BOX = Block.box(2, 0, 2, 14, 14, 14);
	private static final VoxelShape BOUNDING_BOX_I = Block.box(2, 2, 2, 14, 1, 14);

	public HandCrank()
	{
		super("hand_crank", false, Properties.of(Material.WOOD).strength(1.3f),
				new Item.Properties().tab(FAItemGroups.mechanical));
		TileEntityHandler.tiles.add(TEHandCrank.class);
		registerDefaultState(stateDefinition.any().setValue(INVERTED, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return context.getHorizontalDirection() == Direction.DOWN ? defaultBlockState().setValue(INVERTED, true) : defaultBlockState();
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
		return new TEHandCrank();
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return the result type of using the block.
	 */
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit)
	{
		if (!world.isClientSide)
		{
			TileEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof TEHandCrank)
			{
				TEHandCrank crank = (TEHandCrank) tileEntity;

				crank.Rotate();
				player.causeFoodExhaustion(0.8f);
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(INVERTED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		if (state.getValue(INVERTED))
			return BOUNDING_BOX_I;
		return BOUNDING_BOX;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return VoxelShapes.empty();
	}
}
