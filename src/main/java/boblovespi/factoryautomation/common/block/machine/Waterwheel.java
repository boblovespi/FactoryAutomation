package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEWaterwheel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

import static net.minecraft.core.Direction.AxisDirection.POSITIVE;

public class Waterwheel extends FABaseBlock implements EntityBlock
{
	public static final EnumProperty<Axis> AXIS = EnumProperty.create("axis", Axis.class, Axis::isHorizontal);
	public static final BooleanProperty MULTIBLOCK_COMPLETE = BooleanProperty.create("multiblock_complete");

	public Waterwheel()
	{
		super("waterwheel", false,
				Properties.of(Materials.WOOD_MACHINE).strength(1, 10).requiresCorrectToolForDrops(), new Item.Properties().tab(FAItemGroups.mechanical));
		TileEntityHandler.tiles.add(TEWaterwheel.class);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(AXIS, MULTIBLOCK_COMPLETE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(AXIS, context.getHorizontalDirection().getAxis());
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TEWaterwheel(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return ITickable::tickTE;
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return the result type of using the block.
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit)
	{
		if (!world.isClientSide)
		{
			BlockEntity te = world.getBlockEntity(pos);
			if (te instanceof TEWaterwheel)
			{
				TEWaterwheel waterwheel = (TEWaterwheel) te;
				Axis axis = state.getValue(AXIS);
				if (IsComplete(world, pos, axis))
					waterwheel.CreateStructure();
				else
					waterwheel.BreakStructure();
			}
		}
		return InteractionResult.SUCCESS;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private boolean MatchesStair(BlockState state, Direction dir, Half half)
	{
		return state.getBlock() == FABlocks.multiblockPart || (state.getBlock() == Blocks.OAK_STAIRS && state.getValue(StairBlock.FACING) == dir.getOpposite()
				&& state.getValue(StairBlock.HALF) == half);
	}

	private boolean IsComplete(Level world, BlockPos pos, Axis axis)
	{
		if (MultiblockHelper.IsStructureComplete(world, pos, "waterwheel", Direction.get(POSITIVE, axis)))
		{
			Direction back = Direction.get(POSITIVE, axis).getCounterClockWise();
			Direction front = Direction.get(POSITIVE, axis).getClockWise();

			if (!MatchesStair(world.getBlockState(pos.relative(back).above(2)), back, Half.BOTTOM))
				return false;
			if (!MatchesStair(world.getBlockState(pos.relative(back, 2).above()), back, Half.BOTTOM))
				return false;
			if (!MatchesStair(world.getBlockState(pos.relative(back).below(2)), back, Half.TOP))
				return false;
			if (!MatchesStair(world.getBlockState(pos.relative(back, 2).below()), back, Half.TOP))
				return false;
			if (!MatchesStair(world.getBlockState(pos.relative(front).above(2)), front, Half.BOTTOM))
				return false;
			if (!MatchesStair(world.getBlockState(pos.relative(front, 2).above()), front, Half.BOTTOM))
				return false;
			if (!MatchesStair(world.getBlockState(pos.relative(front).below(2)), front, Half.TOP))
				return false;
			if (!MatchesStair(world.getBlockState(pos.relative(front, 2).below()), front, Half.TOP))
				return false;
			return true;
		}
		return false;
	}
}
