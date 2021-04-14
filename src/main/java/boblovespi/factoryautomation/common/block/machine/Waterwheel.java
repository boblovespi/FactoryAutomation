package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEWaterwheel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
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

import static net.minecraft.util.Direction.AxisDirection.POSITIVE;

/**
 * Created by Willi on 6/23/2019.
 */
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
		return getDefaultState().with(AXIS, context.getPlacementHorizontalFacing().getAxis());
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
		return new TEWaterwheel();
	}

	/**
	 * Called when the block is right clicked by a player.
	 * @return
	 */
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit)
	{
		if (!world.isRemote)
		{
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TEWaterwheel)
			{
				TEWaterwheel waterwheel = (TEWaterwheel) te;
				Axis axis = state.get(AXIS);
				if (IsComplete(world, pos, axis))
					waterwheel.CreateStructure();
				else
					waterwheel.BreakStructure();
			}
		}
		return ActionResultType.SUCCESS;
	}

	private boolean MatchesStair(BlockState state, Direction dir, Half half)
	{
		return state.getBlock() == Blocks.OAK_STAIRS && state.get(StairsBlock.FACING) == dir.getOpposite()
				&& state.get(StairsBlock.HALF) == half;
	}

	private boolean IsComplete(World world, BlockPos pos, Axis axis)
	{
		if (MultiblockHelper.IsStructureComplete(world, pos, "waterwheel", Direction.getFacingFromAxis(POSITIVE, axis)))
		{
			Direction back = Direction.getFacingFromAxis(POSITIVE, axis).rotateYCCW();
			Direction front = Direction.getFacingFromAxis(POSITIVE, axis).rotateY();

			if (!MatchesStair(world.getBlockState(pos.offset(back).up(2)), back, Half.BOTTOM))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(back, 2).up()), back, Half.BOTTOM))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(back).down(2)), back, Half.TOP))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(back, 2).down()), back, Half.TOP))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(front).up(2)), front, Half.BOTTOM))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(front, 2).up()), front, Half.BOTTOM))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(front).down(2)), front, Half.TOP))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(front, 2).down()), front, Half.TOP))
				return false;
			return true;
		}
		return false;
	}
}
