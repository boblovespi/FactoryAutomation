package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.fluid.Pipe;
import boblovespi.factoryautomation.common.block.mechanical.BevelGear;
import boblovespi.factoryautomation.common.tileentity.mechanical.TECreativeMechanicalSource;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import boblovespi.factoryautomation.common.tileentity.pipe.TEPipe;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 8/1/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Wrench extends WorkbenchToolItem
{
	public Wrench(String name, float attackDamageIn, float attackSpeedIn, Tier materialIn)
	{
		super(name, attackDamageIn, attackSpeedIn, materialIn, FATags.NOTHING_TOOL, new Properties());
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
		} else if (block == FABlocks.creativeMechanicalSource)
		{
			if (!world.isClientSide)
			{
				var te = world.getBlockEntity(pos);
				if (te instanceof TECreativeMechanicalSource cms)
				{
					if (context.getHand() == InteractionHand.MAIN_HAND)
					{
						cms.speed += 10 * (context.isSecondaryUseActive() ? -1 : 1) * (context.getClickLocation().y - pos.getY());
						if (cms.speed < 0)
							cms.speed = 0;
					} else
					{
						cms.torque += 10 * (context.isSecondaryUseActive() ? -1 : 1) * (context.getClickLocation().y - pos.getY());
						if (cms.torque < 0)
							cms.torque = 0;
					}
				}
				return InteractionResult.SUCCESS;
			}
		} else if (block instanceof Pipe)
		{
			if (!world.isClientSide)
			{
				var te = world.getBlockEntity(pos);
				if (te instanceof TEPipe pipe)
				{
					var x = context.getClickLocation().x - pos.getX() - 0.5f;
					var y = context.getClickLocation().y - pos.getY() - 0.5f;
					var z = context.getClickLocation().z - pos.getZ() - 0.5f;
					var side = Direction.getNearest(x, y, z);
					// System.out.println(String.format("%f, %f, %f | side: %s", x, y, z, side));
					if (pipe.FlipIONode(side))
						return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}
}
