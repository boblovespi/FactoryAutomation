package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEJawCrusher;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 2/17/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class JawCrusher extends FABaseBlock
{
	public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public JawCrusher()
	{
		super("jaw_crusher", false, Properties.of(Material.METAL).strength(1.5f),
				new Item.Properties().tab(FAItemGroups.mechanical));
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
		TileEntityHandler.tiles.add(TEJawCrusher.class);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public BlockEntity createTileEntity(BlockState meta, BlockGetter levelIn)
	{
		return new TEJawCrusher();
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return the result type of using this block.
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerIn, InteractionHand hand,
			BlockHitResult result)
	{
		if (world.isClientSide)
			return InteractionResult.SUCCESS;

		ItemStack held = playerIn.getItemInHand(hand);
		BlockEntity te = world.getBlockEntity(pos);
		if (held.isEmpty())
		{
			if (te instanceof TEJawCrusher)
				((TEJawCrusher) te).RemovePlate();
		} else
		{
			if (te instanceof TEJawCrusher)
				((TEJawCrusher) te).PlaceWearPlate(held);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
}
