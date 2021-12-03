package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.item.Item.Properties;

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
	public InteractionResult useOn(UseOnContext context)
	{
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Block block = world.getBlockState(pos).getBlock();

		if (block == FABlocks.gearbox)
		{
			if (!world.isClientSide)
			{
				BlockEntity te = world.getBlockEntity(pos);
				if (te instanceof TEGearbox)
				{
					((TEGearbox) te).SwitchGears();
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
