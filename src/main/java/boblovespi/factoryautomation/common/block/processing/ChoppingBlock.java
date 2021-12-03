package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.processing.TEChoppingBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 12/26/2018.
 */
public class ChoppingBlock extends FABaseBlock
{
	private static final VoxelShape BOUNDING_BOX = Block.box(0, 0, 0, 16, 8, 16);
	public final int maxUses;

	public ChoppingBlock(String name, int maxUses, Properties properties)
	{
		super(name, false, properties, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
		this.maxUses = maxUses;
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
		return new TEChoppingBlock(maxUses);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return BOUNDING_BOX;
	}

	/* TODO: move to event handler
	@Override
	public void triggerEvent(BlockState state, World world, BlockPos pos, PlayerEntity player)
	{
		if (world.isClientSide)
			return;
		TileEntity te = world.getBlockEntity(pos);
		if (te instanceof TEChoppingBlock)
			((TEChoppingBlock) te).LeftClick(player.getItemInHandMainhand());
	}
	*/

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit)
	{
		if (world.isClientSide)
			return InteractionResult.SUCCESS;
		ItemStack item = player.getItemInHand(hand);
		BlockEntity te = world.getBlockEntity(pos);
		//		if (item.isEmpty())
		//		{
		//			if (te instanceof TEChoppingBlock)
		//			{
		//				ItemStack taken = ((TEChoppingBlock) te).TakeItem();
		//				ItemHelper.PutItemsInInventoryOrDrop(player, taken, level);
		//			}
		//		} else
		//		{
		//			if (te instanceof TEChoppingBlock)
		//			{
		//				ItemStack stack = ((TEChoppingBlock) te).PlaceItem(item.copy().splitStack(1));
		//				int itemsTaken = 1 - stack.getCount();
		//				item.shrink(itemsTaken);
		//			}
		//		}
		if (te instanceof TEChoppingBlock)
		{
			((TEChoppingBlock) te).TakeOrPlace(item, player);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level levelIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
		{
			BlockEntity te = levelIn.getBlockEntity(pos);

			if (te instanceof TEChoppingBlock)
			{
				((TEChoppingBlock) te).DropItems();
			}

			super.onRemove(state, levelIn, pos, newState, isMoving);
		}
	}
}
