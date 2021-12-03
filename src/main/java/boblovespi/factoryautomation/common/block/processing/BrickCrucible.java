package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.smelting.TEBrickCrucible;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 12/22/2018.
 */
public class BrickCrucible extends FABaseBlock
{
	public static final BooleanProperty MULTIBLOCK_COMPLETE = BooleanProperty.create("multiblock_complete");
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	private static final VoxelShape BOUNDING_BOX = Shapes.box(2 / 16d, 0, 2 / 16d, 14 / 16d, 1, 14 / 16d);

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
	public BlockEntity createTileEntity(BlockState state, BlockGetter level)
	{
		return new TEBrickCrucible();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, MULTIBLOCK_COMPLETE);
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult hit)
	{
		if (!world.isClientSide)
		{
			if (MultiblockHelper.IsStructureComplete(world, pos, TEBrickCrucible.MULTIBLOCK_ID, state.getValue(FACING)))
			{
				BlockEntity te = world.getBlockEntity(pos);
				if (te instanceof TEBrickCrucible)
				{
					TEBrickCrucible foundry = (TEBrickCrucible) te;
					if (!foundry.IsStructureValid())
						foundry.CreateStructure();

					if (hit.getDirection() == state.getValue(FACING).getCounterClockWise())
						foundry.PourInto(hit.getDirection());
					else
						NetworkHooks.openGui((ServerPlayer) player, foundry, pos);
				}
			} else
			{
				BlockEntity te = world.getBlockEntity(pos);
				if (te instanceof TEBrickCrucible)
					((TEBrickCrucible) te).SetStructureInvalid();
			}
		}
		return InteractionResult.SUCCESS;
	}

	/**
	 * Gets the {@link BlockState} to place
	 */
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getCounterClockWise());
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_)
	{
		return BOUNDING_BOX;
	}
}
