package boblovespi.factoryautomation.common.item.crucible;

import boblovespi.factoryautomation.common.item.FABaseItem;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import boblovespi.factoryautomation.common.util.NBTHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by Willi on 4/8/2018.
 */
public abstract class Crucible extends FABaseItem
{
	public Crucible(String unlocalizedName)
	{
		super(unlocalizedName, FACreativeTabs.metallurgy);
		setMaxStackSize(1);
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world,
			EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (NBTHelper.HasKey(stack, "items"))
		{
			ItemStackHandler inv = new ItemStackHandler();
			inv.deserializeNBT(NBTHelper.GetTag(stack).getCompoundTag("items"));
			for (int i = 0; i < inv.getSlots(); ++i)
			{
				if (!playerIn.addItemStackToInventory(inv.getStackInSlot(i)))
				{
					if (!world.isRemote)
						world.spawnEntity(new EntityItem(world, playerIn.posX,
														 playerIn.posY,
														 playerIn.posZ,
														 inv.getStackInSlot(
																 i)));
				}
			}

			return new ActionResult<>(
					EnumActionResult.SUCCESS, new ItemStack(this));
		}

		return new ActionResult<>(EnumActionResult.PASS, stack);
	}
}
