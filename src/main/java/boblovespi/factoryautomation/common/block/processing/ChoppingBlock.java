package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.processing.TEChoppingBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/26/2018.
 */
public class ChoppingBlock extends FABaseBlock
{
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.5d, 1);
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
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TEChoppingBlock();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BOUNDING_BOX;
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player)
	{
		if (world.isRemote)
			return;
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TEChoppingBlock)
			((TEChoppingBlock) te).LeftClick(player.getHeldItemMainhand());
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
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

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if (te instanceof TEChoppingBlock)
		{
			((TEChoppingBlock) te).DropItems();
		}

		super.breakBlock(worldIn, pos, state);
	}
}
