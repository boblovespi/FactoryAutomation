package boblovespi.factoryautomation.common.item.crucible;

import boblovespi.factoryautomation.common.item.FABaseItem;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.NBTHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 4/8/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class Crucible extends FABaseItem
{
	public Crucible(String name)
	{
		super(name, new Properties().tab(FAItemGroups.metallurgy).stacksTo(1));
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> use(World level, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (NBTHelper.HasKey(stack, "items"))
		{
			ItemStackHandler inv = new ItemStackHandler();
			inv.deserializeNBT(NBTHelper.GetTag(stack).getCompound("items"));
			for (int i = 0; i < inv.getSlots(); ++i)
			{
				if (!playerIn.addItem(inv.getStackInSlot(i)))
				{
					if (!world.isClientSide)
						world.addFreshEntity(
								new ItemEntity(world, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
										inv.getStackInSlot(i)));
				}
			}

			return new ActionResult<>(ActionResultType.SUCCESS, new ItemStack(this));
		}

		return new ActionResult<>(ActionResultType.PASS, stack);
	}
}
