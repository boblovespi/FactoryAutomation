package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

/**
 * Created by Willi on 4/12/2017.
 */
public class RiceGrain extends FABaseItem implements IPlantable
{
	public RiceGrain()
	{
		super("rice", CreativeTabs.FOOD);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess iBlockAccess,
			BlockPos blockPos)
	{
		return EnumPlantType.Water;
		// return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess iBlockAccess, BlockPos blockPos)
	{
		return FABlocks.riceCrop.ToBlock().getDefaultState();
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world,
			BlockPos pos, EnumHand hand, EnumFacing facing, float dir, float x,
			float y)
	{
		IBlockState state = world.getBlockState(pos.up());

		ItemStack items = player.getActiveItemStack();
		if (facing == EnumFacing.UP && player
				.canPlayerEdit(pos.offset(facing), facing, items) && state
				.getBlock()
				.canSustainPlant(state, world, pos.up(), EnumFacing.UP, this)
				&& world.isAirBlock(pos.up(2)))
		{
			world.setBlockState(pos.up(2),
								FABlocks.riceCrop.ToBlock().getDefaultState());
			items.shrink(1);

			return EnumActionResult.SUCCESS;
		} else
			return EnumActionResult.FAIL;
	}
}
