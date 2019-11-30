package boblovespi.factoryautomation.common.item.crucible;

import boblovespi.factoryautomation.common.item.FABaseItem;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.NBTHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by Willi on 4/8/2018.
 */
public abstract class Crucible extends FABaseItem
{
	public Crucible(String name)
	{
		super(name, new Properties().group(FAItemGroups.metallurgy).maxStackSize(1));
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (NBTHelper.HasKey(stack, "items"))
		{
			ItemStackHandler inv = new ItemStackHandler();
			inv.deserializeNBT(NBTHelper.GetTag(stack).getCompound("items"));
			for (int i = 0; i < inv.getSlots(); ++i)
			{
				if (!playerIn.addItemStackToInventory(inv.getStackInSlot(i)))
				{
					if (!world.isRemote)
						world.addEntity(new ItemEntity(world, playerIn.posX, playerIn.posY, playerIn.posZ,
								inv.getStackInSlot(i)));
				}
			}

			return new ActionResult<>(ActionResultType.SUCCESS, new ItemStack(this));
		}

		return new ActionResult<>(ActionResultType.PASS, stack);
	}
}
