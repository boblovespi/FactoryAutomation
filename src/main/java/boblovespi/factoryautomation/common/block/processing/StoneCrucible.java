package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCrucible;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/22/2018.
 */
public class StoneCrucible extends FABaseBlock
{
	public static final BooleanProperty MULTIBLOCK_COMPLETE = BooleanProperty.create("multiblock_complete");
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final VoxelShape BOUNDING_BOX = Block.makeCuboidShape(2 / 16d, 0, 2 / 16d, 14 / 16d, 1, 14 / 16d);

	public StoneCrucible()
	{
		super("stone_crucible", false, Properties.create(Material.ROCK).hardnessAndResistance(1.5f).harvestLevel(0)
												 .harvestTool(ToolType.PICKAXE),
				new Item.Properties().group(FAItemGroups.metallurgy));
		setDefaultState(stateContainer.getBaseState().with(MULTIBLOCK_COMPLETE, false).with(FACING, Direction.NORTH));
		TileEntityHandler.tiles.add(TEStoneCrucible.class);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return BOUNDING_BOX;
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
		return new TEStoneCrucible();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(MULTIBLOCK_COMPLETE, FACING);
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return
	 */
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit)
	{
		if (!world.isRemote)
		{
			if (MultiblockHelper.IsStructureComplete(world, pos, TEStoneCrucible.MULTIBLOCK_ID, state.get(FACING)))
			{
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TEStoneCrucible)
				{
					TEStoneCrucible foundry = (TEStoneCrucible) te;
					if (!foundry.IsStructureValid())
						foundry.CreateStructure();

					if (hit.getFace() == state.get(FACING).rotateYCCW())
						foundry.PourInto(hit.getFace());
					else
						NetworkHooks.openGui((ServerPlayerEntity) player, foundry, pos);
				}
			} else
			{
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TEStoneCrucible)
					((TEStoneCrucible) te).SetStructureInvalid();
			}
		}
		return ActionResultType.SUCCESS;
	}

	/**
	 * Gets the {@link BlockState} to place
	 */
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().rotateYCCW());
	}
}
