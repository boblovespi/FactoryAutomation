package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEHandCrank;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

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
	private static final VoxelShape BOUNDING_BOX_I = Block.box(2, 2, 2, 14, 16, 14);

	public HandCrank()
	{
		super("hand_crank", false, Properties.of(Material.WOOD).strength(1.3f),
				new Item.Properties().tab(FAItemGroups.mechanical));
		TileEntityHandler.tiles.add(TEHandCrank.class);
		registerDefaultState(stateDefinition.any().setValue(INVERTED, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return context.getNearestLookingDirection() == Direction.UP ? defaultBlockState().setValue(INVERTED, true) : defaultBlockState();
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
		return new TEHandCrank();
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return the result type of using the block.
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult hit)
	{
		if (!world.isClientSide)
		{
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof TEHandCrank)
			{
				TEHandCrank crank = (TEHandCrank) tileEntity;

				crank.Rotate();
				player.causeFoodExhaustion(0.8f);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(INVERTED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		if (state.getValue(INVERTED))
			return BOUNDING_BOX_I;
		return BOUNDING_BOX;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter levelIn, BlockPos pos)
	{
		return Shapes.empty();
	}
}
