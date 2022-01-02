package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TETripHammerController;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 8/13/2018.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TripHammerController extends FABaseBlock implements EntityBlock
{
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<BlockstateEnum> MULTIBLOCK_COMPLETE = EnumProperty
			.create("multiblock_complete", BlockstateEnum.class);

	public TripHammerController()
	{
		super("trip_hammer", false, Properties.of(Material.METAL).strength(3, 20).requiresCorrectToolForDrops(),
				new Item.Properties().tab(FAItemGroups.metallurgy));
		TileEntityHandler.tiles.add(TETripHammerController.class);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, MULTIBLOCK_COMPLETE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection())
				   .setValue(MULTIBLOCK_COMPLETE, BlockstateEnum.FALSE);
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
		if (world.isClientSide)
			return InteractionResult.SUCCESS;
		BlockEntity te1 = world.getBlockEntity(pos);
		if (te1 instanceof TETripHammerController)
		{
			TETripHammerController te = (TETripHammerController) te1;
			if (MultiblockHelper.IsStructureComplete(world, pos, TETripHammerController.MULTIBLOCK_ID,
					world.getBlockState(pos).getValue(FACING)))
			{
				System.out.println("complete!");
				if (!te.IsStructureValid())
					te.CreateStructure();
				else
				{
					ItemStack item = player.getItemInHand(hand);
					if (item.isEmpty())
					{
						ItemStack item1 = te.TakeItem();
						ItemHelper.PutItemsInInventoryOrDrop(player, item1, world);
					} else
					{
						if (te.PutItem(item.copy().split(1)))
							item.shrink(1);
					}
				}
			}
			else
				te.BreakStructure();
		} else
			System.out.println("incomplete!");
		if (te1 == null)
			System.out.println("null!!!");
		return InteractionResult.SUCCESS;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TETripHammerController(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return ITickable::tickTE;
	}

	@SuppressWarnings("SpellCheckingInspection")
	public enum BlockstateEnum implements StringRepresentable {
		FALSE, TRUE, TESR;

		@Override
		public String getSerializedName() {
			return name().toLowerCase();
		}
	}
}
