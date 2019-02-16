package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.recipe.MillstoneRecipe;
import boblovespi.factoryautomation.common.tileentity.TEMachine;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;

/**
 * Created by Willi on 2/12/2019.
 */
public class TEMillstone extends TEMachine
{
	public float rotation = 0;
	private MechanicalUser mechanicalUser;
	private MillstoneRecipe millstoneRecipe = null;
	private int counter = 0;

	public TEMillstone()
	{
		super(0);
		mechanicalUser = new MechanicalUser(EnumSet.of(EnumFacing.DOWN));
	}

	@Override
	protected void Update()
	{
		++counter;
		counter %= 4;

		if (counter == 0)
		{
			TileEntity te = world.getTileEntity(pos.down());
			EnumFacing facing = EnumFacing.UP;
			if (TEHelper.IsMechanicalFace(te, facing))
			{
				mechanicalUser.SetSpeedOnFace(EnumFacing.DOWN, GetUser(te, facing).GetSpeedOnFace(facing));
				mechanicalUser.SetTorqueOnFace(EnumFacing.DOWN, GetUser(te, facing).GetTorqueOnFace(facing));
			} else
			{
				mechanicalUser.SetSpeedOnFace(EnumFacing.DOWN, 0);
				mechanicalUser.SetTorqueOnFace(EnumFacing.DOWN, 0);
			}

			markDirty();
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override
	protected void UpdateClient()
	{
		rotation = (rotation + mechanicalUser.GetSpeed()) % 360;
	}

	@Nonnull
	@Override
	protected String FindRecipe()
	{
		MillstoneRecipe recipe1 = MillstoneRecipe.FindRecipe(processingInv.getStackInSlot(0));
		if (recipe1 == null)
		{
			millstoneRecipe = null;
			return "none";
		} else
		{
			millstoneRecipe = recipe1;
			return recipe1.GetName();
		}
	}

	@Override
	protected int GetMaxProgress()
	{
		return millstoneRecipe.GetTime();
	}

	@Override
	protected void OnRecipeComplete(String recipe)
	{
		for (ItemStack stack : millstoneRecipe.GetOutputs())
		{
			EntityItem item = new EntityItem(world, pos.getX() - 0.5 + world.rand.nextInt(3), pos.getY() - 0.1,
					pos.getZ() - 0.5 + world.rand.nextInt(3), stack.copy());
			item.addVelocity(world.rand.nextDouble() - 0.5, 0, world.rand.nextDouble() - 0.5);
			item.velocityChanged = true;
			world.spawnEntity(item);
		}
		processingInv.extractItem(0, 1, false);
	}

	@Override
	protected float GetProgressScalar()
	{
		if (mechanicalUser.GetTorque() >= millstoneRecipe.GetTorque())
			return MathHelper.clamp(mechanicalUser.GetSpeed() / 10f, 0, 10);
		else
			return 0;
	}

	@Override
	protected void ReadCustomNBT(NBTTagCompound tag)
	{
		mechanicalUser.ReadFromNBT(tag.getCompoundTag("mechanicalUser"));
		millstoneRecipe = MillstoneRecipe.GetRecipe(recipe);
	}

	@Override
	protected void WriteCustomNBT(NBTTagCompound tag)
	{
		tag.setTag("mechanicalUser", mechanicalUser.WriteToNBT());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return facing == EnumFacing.UP;
		else if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return facing == EnumFacing.DOWN;
		return false;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) processingInv;
		else if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return (T) mechanicalUser;
		return null;
	}

	public float GetSpeed()
	{
		return mechanicalUser.GetSpeed();
	}

	public void TakeOrPlace(ItemStack item, EntityPlayer player)
	{
		if (!processingInv.getStackInSlot(0).isEmpty())
		{
			ItemStack taken = processingInv.extractItem(0, 64, false);
			ItemHelper.PutItemsInInventoryOrDrop(player, taken, world);
		} else
		{
			ItemStack stack = processingInv.insertItem(0, item.copy(), false);
			item.setCount(stack.getCount());
		}

		markDirty();
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}
}
