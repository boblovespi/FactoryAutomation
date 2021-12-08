package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TESteelmakingFurnace;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 12/23/2017.
 */
@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SteelmakingFurnaceController extends FABaseBlock implements EntityBlock
{
	public static final EnumProperty<Axis> AXIS = EnumProperty.create("axis", Axis.class, Axis.X, Axis.Z);
	public static final BooleanProperty MULTIBLOCK_COMPLETE = BooleanProperty.create("multiblock_complete");

	public SteelmakingFurnaceController()
	{
		super(Material.EGG, "steelmaking_furnace_controller", null);
	}

	public String GetPatternId()
	{
		return "steelmaking_furnace";
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TESteelmakingFurnace(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state,
																  BlockEntityType<T> type)
	{
		return ITickable::tickTE;
	}

	/**
	 * Called when the block is right clicked by a player.
	 *
	 * @return the block action use result.
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand handIn, BlockHitResult hit)
	{
		if (!world.isClientSide)
		{
			if (MultiblockHelper.IsStructureComplete(world, pos, GetPatternId(),
					world.getBlockState(pos).getValue(AXIS) == Axis.X ? Direction.WEST : Direction.NORTH) /*|| MultiblockHelper
					.IsStructureComplete(worldIn, pos, GetPatternId(),
										 Direction.NORTH)*/)
			{
				BlockEntity te = world.getBlockEntity(pos);
				if (te instanceof TESteelmakingFurnace tesf)
				{
					tesf.CreateStructure();
					NetworkHooks.openGui((ServerPlayer) player, TEHelper.GetContainer(te), pos);
				}
			} else
			{
				BlockEntity te = world.getBlockEntity(pos);
				if (te instanceof TESteelmakingFurnace tesf)
					tesf.SetStructureInvalid();
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState().setValue(AXIS, context.getHorizontalDirection().getAxis());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
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