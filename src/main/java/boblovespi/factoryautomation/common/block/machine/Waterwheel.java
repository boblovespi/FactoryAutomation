package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEWaterwheel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.Half;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.util.Direction.AxisDirection.POSITIVE;

/**
 * Created by Willi on 6/23/2019.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(AXIS, MULTIBLOCK_COMPLETE);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
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
	public TileEntity createTileEntity(BlockState state, IBlockReader level)
	{
		return new TEWaterwheel();
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return the result type of using the block.
	 */
	@Override
	public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit)
	{
		if (!level.isClientSide)
		{
			TileEntity te = level.getBlockEntity(pos);
			if (te instanceof TEWaterwheel)
			{
				TEWaterwheel waterwheel = (TEWaterwheel) te;
				Axis axis = state.getValue(AXIS);
				if (IsComplete(level, pos, axis))
					waterwheel.createStructure();
				else
					waterwheel.breakStructure();
			}
		}
		return ActionResultType.SUCCESS;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private boolean MatchesStair(BlockState state, Direction dir, Half half)
	{
		return state.getBlock() == Blocks.OAK_STAIRS && state.getValue(StairsBlock.FACING) == dir.getOpposite()
				&& state.getValue(StairsBlock.HALF) == half;
	}

	private boolean IsComplete(World level, BlockPos pos, Axis axis)
	{
		if (MultiblockHelper.IsStructureComplete(level, pos, "waterwheel", Direction.get(POSITIVE, axis)))
		{
			Direction back = Direction.get(POSITIVE, axis).getCounterClockWise();
			Direction front = Direction.get(POSITIVE, axis).getClockWise();

			if (!MatchesStair(level.getBlockState(pos.relative(back).above(2)), back, Half.BOTTOM))
				return false;
			if (!MatchesStair(level.getBlockState(pos.relative(back, 2).above()), back, Half.BOTTOM))
				return false;
			if (!MatchesStair(level.getBlockState(pos.relative(back).below(2)), back, Half.TOP))
				return false;
			if (!MatchesStair(level.getBlockState(pos.relative(back, 2).below()), back, Half.TOP))
				return false;
			if (!MatchesStair(level.getBlockState(pos.relative(front).above(2)), front, Half.BOTTOM))
				return false;
			if (!MatchesStair(level.getBlockState(pos.relative(front, 2).above()), front, Half.BOTTOM))
				return false;
			if (!MatchesStair(level.getBlockState(pos.relative(front).below(2)), front, Half.TOP))
				return false;
			if (!MatchesStair(level.getBlockState(pos.relative(front, 2).below()), front, Half.TOP))
				return false;
			return true;
		}
		return false;
	}
}
