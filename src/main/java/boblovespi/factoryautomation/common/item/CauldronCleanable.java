package boblovespi.factoryautomation.common.item;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Willi on 4/10/2018.
 */
public class CauldronCleanable extends FABaseItem
{
	private final ItemStack cleanedInto;

	public CauldronCleanable(String unlocalizedName, CreativeTabs ct,
			ItemStack cleanedInto)
	{
		super(unlocalizedName, ct);
		this.cleanedInto = cleanedInto;
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn,
			BlockPos pos, EnumHand hand, EnumFacing facing, float hitX,
			float hitY, float hitZ)
	{
		IBlockState state = worldIn.getBlockState(pos);
		ItemStack item = player.getHeldItem(hand);

		if (state.getBlock() != Blocks.CAULDRON)
			return EnumActionResult.PASS;

		int i = state.getValue(BlockCauldron.LEVEL);

		if (i > 0)
		{
			if (!worldIn.isRemote)
			{
				item.shrink(1);

				if (!player.addItemStackToInventory(cleanedInto.copy()))
					worldIn.spawnEntity(
							new EntityItem(worldIn, player.posX, player.posY,
										   player.posZ, cleanedInto.copy()));

				Blocks.CAULDRON.setWaterLevel(worldIn, pos, state, i - 1);
				player.addStat(StatList.ARMOR_CLEANED);
			}

			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}
}
