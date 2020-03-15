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
	private static final VoxelShape BOUNDING_BOX = Block.makeCuboidShape(0, 0, 0, 1, 0.5d, 1);
	public final int maxUses;

	public ChoppingBlock(String name, int maxUses, Properties properties)
	{
		super(name, false, properties, new Item.Properties().group(ItemGroup.DECORATIONS));
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
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TEChoppingBlock();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return BOUNDING_BOX;
	}

	@Override
	public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player)
	{
		if (world.isRemote)
			return;
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TEChoppingBlock)
			((TEChoppingBlock) te).LeftClick(player.getHeldItemMainhand());
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit)
	{
		if (world.isRemote)
			return true;
		ItemStack item = player.getHeldItem(hand);
		TileEntity te = world.getTileEntity(pos);
		//		if (item.isEmpty())
		//		{
		//			if (te instanceof TEChoppingBlock)
		//			{
		//				ItemStack taken = ((TEChoppingBlock) te).TakeItem();
		//				ItemHelper.PutItemsInInventoryOrDrop(player, taken, world);
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
		return true;
	}

	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
		{
			TileEntity te = worldIn.getTileEntity(pos);

			if (te instanceof TEChoppingBlock)
			{
				((TEChoppingBlock) te).DropItems();
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
}
