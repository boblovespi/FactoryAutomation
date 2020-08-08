package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.TESteelmakingFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/23/2017.
 */
public class SteelmakingFurnaceController extends FABaseBlock
{
	public static final EnumProperty<Axis> AXIS = EnumProperty.create("axis", Axis.class, Axis.X, Axis.Z);
	public static final BooleanProperty MULTIBLOCK_COMPLETE = BooleanProperty.create("multiblock_complete");

	public SteelmakingFurnaceController()
	{
		super(Material.DRAGON_EGG, "steelmaking_furnace_controller", null);
	}

	public String GetPatternId()
	{
		return "steelmaking_furnace";
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TESteelmakingFurnace();
	}

	/**
	 * Called when the block is right clicked by a player.
	 * @return
	 */
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit)
	{
		if (!worldIn.isRemote)
		{
			if (MultiblockHelper.IsStructureComplete(worldIn, pos, GetPatternId(),
					worldIn.getBlockState(pos).get(AXIS) == Axis.X ? Direction.WEST : Direction.NORTH) /*|| MultiblockHelper
					.IsStructureComplete(worldIn, pos, GetPatternId(),
										 Direction.NORTH)*/)
			{
				TileEntity te = worldIn.getTileEntity(pos);
				if (te instanceof TESteelmakingFurnace)
					((TESteelmakingFurnace) te).CreateStructure();
				// TODO: GUIS
				//				playerIn.openGui(FactoryAutomation.instance, GuiHandler.GuiID.STEELMAKING_FURNACE.id, worldIn,
				//						pos.getX(), pos.getY(), pos.getZ());
			} else
			{
				TileEntity te = worldIn.getTileEntity(pos);
				if (te instanceof TESteelmakingFurnace)
					((TESteelmakingFurnace) te).SetStructureInvalid();
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(AXIS, context.getPlacementHorizontalFacing().getAxis());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(AXIS, MULTIBLOCK_COMPLETE);
	}
}

/*
Recipes: (air) + (fuel)

*ALL RECIPES TAKE PIG IRON*

nothing -> slowest
air -> slower
preheated air -> slow
preheated air + fuel -> fast
preheated oxygen -> medium, better efficiency

preheated air + fuel + flux -> fast, better efficiency

*/