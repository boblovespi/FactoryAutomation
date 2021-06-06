package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.processing.TEChoppingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/26/2018.
 */
public class ChoppingBlock extends FABaseBlock
{
	private static final VoxelShape BOUNDING_BOX = Block.box(0, 0, 0, 16, 8, 16);
	public final int maxUses;

	public ChoppingBlock(String name, int maxUses, Properties properties)
	{
		super(name, false, properties, new Item.Properties().tab(ItemGroup.DECORATIONS));
		this.maxUses = maxUses;
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		return "processing/" + registryName();
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
		return new TEChoppingBlock(maxUses);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader levelIn, BlockPos pos, ISelectionContext context)
	{
		return BOUNDING_BOX;
	}

	@Override
	public void onBlockClicked(BlockState state, World level, BlockPos pos, PlayerEntity player)
	{
		if (level.isClientSide)
			return;
		TileEntity te = level.getBlockEntity(pos);
		if (te instanceof TEChoppingBlock)
			((TEChoppingBlock) te).LeftClick(player.getItemInHandMainhand());
	}

	@Override
	public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit)
	{
		if (level.isClientSide)
			return ActionResultType.SUCCESS;
		ItemStack item = player.getItemInHand(hand);
		TileEntity te = level.getBlockEntity(pos);
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
		return ActionResultType.SUCCESS;
	}

	public void onReplaced(BlockState state, World levelIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
		{
			TileEntity te = levelIn.getBlockEntity(pos);

			if (te instanceof TEChoppingBlock)
			{
				((TEChoppingBlock) te).DropItems();
			}

			super.onReplaced(state, levelIn, pos, newState, isMoving);
		}
	}
}
