package boblovespi.factoryautomation.common.item.crucible;

import boblovespi.factoryautomation.common.item.FABaseItem;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.NBTHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.item.Item.Properties;

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
	public InteractionResultHolder<ItemStack> use(Level world, Player playerIn, InteractionHand handIn)
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

			return new InteractionResultHolder<>(InteractionResult.SUCCESS, new ItemStack(this));
		}

		return new InteractionResultHolder<>(InteractionResult.PASS, stack);
	}
}
