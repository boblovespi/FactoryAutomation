package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEWaterwheel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.util.Direction.AxisDirection.POSITIVE;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class Waterwheel extends FABaseBlock
{
	public static final EnumProperty<Axis> AXIS = EnumProperty.create("axis", Axis.class, Axis::isHorizontal);
	public static final BooleanProperty MULTIBLOCK_COMPLETE = BooleanProperty.create("multiblock_complete");

	public Waterwheel()
	{
		super("waterwheel", false,
				Properties.of(Materials.WOOD_MACHINE).strength(1, 10).harvestLevel(0)
						  .harvestTool(ToolType.AXE), new Item.Properties().tab(FAItemGroups.mechanical));
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

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter level)
	{
		return new TEWaterwheel();
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
		return state.getBlock() == Blocks.OAK_STAIRS && state.getValue(StairBlock.FACING) == dir.getOpposite()
				&& state.getValue(StairBlock.HALF) == half;
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
