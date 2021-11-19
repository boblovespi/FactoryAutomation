package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.smelting.TEBrickCrucible;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/22/2018.
 */
public class BrickCrucible extends FABaseBlock
{
	public static final BooleanProperty MULTIBLOCK_COMPLETE = BooleanProperty.create("multiblock_complete");
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	private static final VoxelShape BOUNDING_BOX = VoxelShapes.box(2 / 16d, 0, 2 / 16d, 14 / 16d, 1, 14 / 16d);

	public BrickCrucible()
	{
		super("brick_crucible", false, Properties.of(Material.STONE).strength(1.5f).harvestLevel(0)
												 .harvestTool(ToolType.PICKAXE),
				new Item.Properties().tab(FAItemGroups.metallurgy));
		TileEntityHandler.tiles.add(TEBrickCrucible.class);
		registerDefaultState(defaultBlockState().setValue(MULTIBLOCK_COMPLETE, false));
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
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
		return new TEBrickCrucible();
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, MULTIBLOCK_COMPLETE);
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return
	 */
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit)
	{
		if (!world.isClientSide)
		{
			if (MultiblockHelper.IsStructureComplete(world, pos, TEBrickCrucible.MULTIBLOCK_ID, state.getValue(FACING)))
			{
				TileEntity te = world.getBlockEntity(pos);
				if (te instanceof TEBrickCrucible)
				{
					TEBrickCrucible foundry = (TEBrickCrucible) te;
					if (!foundry.IsStructureValid())
						foundry.CreateStructure();

					if (hit.getDirection() == state.getValue(FACING).getCounterClockWise())
						foundry.PourInto(hit.getDirection());
					else
						NetworkHooks.openGui((ServerPlayerEntity) player, foundry, pos);
				}
			} else
			{
				TileEntity te = world.getBlockEntity(pos);
				if (te instanceof TEBrickCrucible)
					((TEBrickCrucible) te).SetStructureInvalid();
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
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getCounterClockWise());
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_)
	{
		return BOUNDING_BOX;
	}
}
