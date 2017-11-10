package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.block.FABlock;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Willi on 11/9/2017.
 */
@Deprecated
public class FAItemBlock extends FABaseItem
{

	private final Block block;
	public final FABlock faBlock;

	public FAItemBlock(FABlock base)
	{
		super(base.UnlocalizedName(),
				base.ToBlock().getCreativeTabToDisplayOn());
		this.block = base.ToBlock();
		this.faBlock = base;
	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn,
			BlockPos pos, EnumHand hand, EnumFacing facing, float hitX,
			float hitY, float hitZ)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if (!block.isReplaceable(worldIn, pos))
		{
			pos = pos.offset(facing);
		}

		ItemStack itemstack = player.getHeldItem(hand);

		if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack)
				&& worldIn
				.mayPlace(this.block, pos, false, facing, null))
		{
			int i = this.getMetadata(itemstack.getMetadata());
			IBlockState iblockstate1 = this.block
					.getStateForPlacement(worldIn, pos, facing, hitX, hitY,
							hitZ, i, player, hand);

			if (placeBlockAt(itemstack, player, worldIn, pos, facing, hitX,
					hitY, hitZ, iblockstate1))
			{
				iblockstate1 = worldIn.getBlockState(pos);
				SoundType soundtype = iblockstate1.getBlock()
						.getSoundType(iblockstate1, worldIn, pos, player);
				worldIn.playSound(player, pos, soundtype.getPlaceSound(),
						SoundCategory.BLOCKS,
						(soundtype.getVolume() + 1.0F) / 2.0F,
						soundtype.getPitch() * 0.8F);
				itemstack.shrink(1);
			}

			return EnumActionResult.SUCCESS;
		} else
		{
			return EnumActionResult.FAIL;
		}
	}

	public static boolean setTileEntityNBT(World worldIn,
			@Nullable EntityPlayer player, BlockPos pos, ItemStack stackIn)
	{
		MinecraftServer minecraftserver = worldIn.getMinecraftServer();

		if (minecraftserver == null)
		{
			return false;
		} else
		{
			NBTTagCompound nbttagcompound = stackIn
					.getSubCompound("BlockEntityTag");

			if (nbttagcompound != null)
			{
				TileEntity tileentity = worldIn.getTileEntity(pos);

				if (tileentity != null)
				{
					if (!worldIn.isRemote && tileentity.onlyOpsCanSetNbt() && (
							player == null || !player.canUseCommandBlock()))
					{
						return false;
					}

					NBTTagCompound nbttagcompound1 = tileentity
							.writeToNBT(new NBTTagCompound());
					NBTTagCompound nbttagcompound2 = nbttagcompound1.copy();
					nbttagcompound1.merge(nbttagcompound);
					nbttagcompound1.setInteger("x", pos.getX());
					nbttagcompound1.setInteger("y", pos.getY());
					nbttagcompound1.setInteger("z", pos.getZ());

					if (!nbttagcompound1.equals(nbttagcompound2))
					{
						tileentity.readFromNBT(nbttagcompound1);
						tileentity.markDirty();
						return true;
					}
				}
			}

			return false;
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos,
			EnumFacing side, EntityPlayer player, ItemStack stack)
	{
		Block block = worldIn.getBlockState(pos).getBlock();

		if (block == Blocks.SNOW_LAYER && block.isReplaceable(worldIn, pos))
		{
			side = EnumFacing.UP;
		} else if (!block.isReplaceable(worldIn, pos))
		{
			pos = pos.offset(side);
		}

		return worldIn.mayPlace(this.block, pos, false, side, null);
	}

	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
			float hitZ, IBlockState newState)
	{
		if (!world.setBlockState(pos, newState, 11))
			return false;

		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == this.block)
		{
			setTileEntityNBT(world, player, pos, stack);
			this.block.onBlockPlacedBy(world, pos, state, player, stack);

			if (player instanceof EntityPlayerMP)
				CriteriaTriggers.PLACED_BLOCK
						.trigger((EntityPlayerMP) player, pos, stack);
		}

		return true;
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return this.block.getUnlocalizedName();
	}

	/**
	 * Returns the unlocalized name of this item.
	 */
	public String getUnlocalizedName()
	{
		return this.block.getUnlocalizedName();
	}

	/**
	 * gets the CreativeTab this item is displayed on
	 */
	public CreativeTabs getCreativeTab()
	{
		return this.block.getCreativeTabToDisplayOn();
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (this.isInCreativeTab(tab))
		{
			this.block.getSubBlocks(tab, items);
		}
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn,
			List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		this.block.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
