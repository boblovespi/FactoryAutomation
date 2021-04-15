package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 8/1/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
	public ActionResultType useOn(ItemUseContext context)
	{
		World level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Block block = level.getBlockState(pos).getBlock();

		if (block == FABlocks.gearbox)
		{
			if (!world.isClientSide)
			{
				TileEntity te = level.getBlockEntity(pos);
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
