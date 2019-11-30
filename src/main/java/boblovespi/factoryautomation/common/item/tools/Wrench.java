package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import net.minecraft.block.Block;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Willi on 8/1/2018.
 */
public class Wrench extends WorkbenchToolItem
{
	public Wrench(String name, float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn)
	{
		super(name, attackDamageIn, attackSpeedIn, materialIn, new Properties(), FAToolTypes.NONE);
	}

	/**
	 * This is called when the item is used, before the block is activated.
	 */
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		World world = context.getWorld();
		BlockPos pos = context.getPos();
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
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
