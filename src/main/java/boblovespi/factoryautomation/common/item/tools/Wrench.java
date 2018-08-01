package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Willi on 8/1/2018.
 */
public class Wrench extends WorkbenchToolItem
{
	public Wrench(String name, float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn)
	{
		super(name, attackDamageIn, attackSpeedIn, materialIn);
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ)
	{
		//		Block block = world.getBlockState(pos).getBlock();
		//
		//		if (block == FABlocks.gearbox)
		//		{
		//			if (!world.isRemote)
		//			{
		//				TileEntity te = world.getTileEntity(pos);
		//				if (te instanceof TEGearbox)
		//				{
		//					((TEGearbox) te).SwitchGears();
		//				}
		//			}
		//
		//			return EnumActionResult.SUCCESS;
		//		}

		return EnumActionResult.PASS;
	}

	/**
	 * This is called when the item is used, before the block is activated.
	 *
	 * @param player The Player that used the item
	 * @param world  The Current World
	 * @param pos    Target position
	 * @param side   The side of the target hit
	 * @param hand   Which hand the item is being held in.  @return Return PASS to allow vanilla handling, any other to skip normal code.
	 */
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand)
	{
		Block block = world.getBlockState(pos).getBlock();

		if (block == FABlocks.gearbox)
		{
			if (!world.isRemote)
			{
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof TEGearbox)
				{
					((TEGearbox) te).SwitchGears();
				}
			}

			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}
}
