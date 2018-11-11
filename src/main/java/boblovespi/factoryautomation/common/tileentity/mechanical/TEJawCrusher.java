package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.api.recipe.JawCrusherRecipe;
import boblovespi.factoryautomation.common.block.machine.JawCrusher;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MachineTiers;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;

/**
 * Created by Willi on 2/17/2018.
 */
public class TEJawCrusher extends FAMachine implements IMechanicalUser
{
	private static final int WEAR_PLATE_SLOT = 2;
	private static final int INPUT_SLOT = 0;
	private static final int OUTPUT_SLOT = 1;
	private static float processingScalar = 1; // how fast currentProcessingTime decreases
	private String currentRecipe = "none";
	private float speed = 0;
	private float torque = 0;
	private JawCrusherRecipe recipe;
	private float currentProcessingTime = 0; // how much time (goes down to 0) left to process

	public TEJawCrusher()
	{
		super("jaw_crusher", new ItemStackHandler(3));

		inputs.put(EnumFacing.UP, new int[] { INPUT_SLOT });
		inputs.put(EnumFacing.DOWN, new int[] {});
		inputs.put(EnumFacing.WEST, new int[] {});
		inputs.put(EnumFacing.EAST, new int[] {});
		inputs.put(EnumFacing.NORTH, new int[] {});
		inputs.put(EnumFacing.SOUTH, new int[] {});

		outputs.put(EnumFacing.UP, new int[] {});
		outputs.put(EnumFacing.WEST, new int[] {});
		outputs.put(EnumFacing.EAST, new int[] {});
		outputs.put(EnumFacing.SOUTH, new int[] {});
		outputs.put(EnumFacing.NORTH, new int[] {});
		outputs.put(EnumFacing.DOWN, new int[] { OUTPUT_SLOT });

	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if (world.isRemote)
			return;
		EnumFacing facing = world.getBlockState(pos).getValue(JawCrusher.FACING).rotateYCCW();
		TileEntity te = world.getTileEntity(pos.offset(facing));
		if (TEHelper.IsMechanicalFace(te, facing))
		{
			speed = GetUser(te, facing).GetSpeedOnFace(facing);
			torque = GetUser(te, facing).GetTorqueOnFace(facing);
		} else
		{
			speed = 0;
			torque = 0;
		}

		/* Begin crafting logic */

		if (currentRecipe.equals("none"))
		{ // no current recipe, find one
			if (!inventory.getStackInSlot(WEAR_PLATE_SLOT).isEmpty() && !inventory.getStackInSlot(INPUT_SLOT).isEmpty())
			{
				JawCrusherRecipe tRecipe = JawCrusherRecipe
						.FindRecipe(inventory.getStackInSlot(INPUT_SLOT), GetMachineTier());

				if (tRecipe != null)
				{
					inventory.getStackInSlot(INPUT_SLOT).shrink(1);
					recipe = tRecipe;
					currentRecipe = recipe.name;
					currentProcessingTime = recipe.time;
				} else
				{ // the recipe doesn't exist, so put the item in the output slot if we can

					ItemStack itemStack = inventory
							.insertItem(OUTPUT_SLOT, inventory.getStackInSlot(INPUT_SLOT).copy().splitStack(1), false);
					inventory.extractItem(INPUT_SLOT, 1 - itemStack.getCount(), false);
				}
			}
		} else
		{
			if (recipe == null)
			{
				recipe = JawCrusherRecipe.GetRecipe(currentRecipe);
			}

			if (speed > recipe.speedReq / 2 && torque > recipe.torqueReq)
			{
				currentProcessingTime -= MathHelper.clamp(speed / recipe.speedReq, 0.5f, 20) * processingScalar;
			}

			if (currentProcessingTime < 0)
			{
				inventory.extractItem(INPUT_SLOT, 1, false);
				inventory.insertItem(OUTPUT_SLOT, recipe.GetOutput((float) Math.random()), false);
				recipe = null;
				currentRecipe = "none";
			}

		}

		/* end crafting logic */

		markDirty();
		/* IMPORTANT */
		IBlockState state2 = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state2, state2, 3);
	}

	private int GetMachineTier()
	{
		Item item = inventory.getStackInSlot(WEAR_PLATE_SLOT).getItem();
		if (item == FAItems.wearPlate.GetItem(MachineTiers.IRON))
			return 0;
		else if (item == FAItems.wearPlate.GetItem(MachineTiers.OBSIDIAN))
			return 1;
		else
			return -1;
	}

	@Override
	public boolean HasConnectionOnSide(EnumFacing side)
	{
		return side == world.getBlockState(pos).getValue(JawCrusher.FACING).rotateYCCW();
	}

	@Override
	public float GetSpeedOnFace(EnumFacing side)
	{
		return HasConnectionOnSide(side) ? speed : 0;
	}

	@Override
	public float GetTorqueOnFace(EnumFacing side)
	{
		return HasConnectionOnSide(side) ? torque : 0;
	}

	@Override
	public void SetSpeedOnFace(EnumFacing side, float speed)
	{
		if (HasConnectionOnSide(side))
			this.speed = speed;
	}

	@Override
	public void SetTorqueOnFace(EnumFacing side, float torque)
	{
		if (HasConnectionOnSide(side))
			this.torque = torque;
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
	 * guis use Slot.isItemValid
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return index == INPUT_SLOT && inventory.insertItem(index, stack.copy().splitStack(1), true).isEmpty();
	}

	@Override
	protected void ReadCustomNBT(NBTTagCompound tag)
	{
		speed = tag.getFloat("speed");
		torque = tag.getFloat("torque");
		currentRecipe = tag.getString("recipe");
		currentProcessingTime = tag.getFloat("processTime");
	}

	@Override
	protected void WriteCustomNBT(NBTTagCompound tag)
	{
		tag.setFloat("speed", speed);
		tag.setFloat("torque", torque);
		tag.setString("recipe", currentRecipe);
		tag.setFloat("processTime", currentProcessingTime);
	}

	public void PlaceWearPlate(ItemStack wearPlate)
	{
		if (FAItems.wearPlate.Contains(wearPlate.getItem()))
		{
			if (inventory.getStackInSlot(WEAR_PLATE_SLOT).isEmpty())
			{
				inventory.insertItem(WEAR_PLATE_SLOT, wearPlate.copy().splitStack(1), false);
				wearPlate.shrink(1);
			}
		}
	}

	public void RemovePlate()
	{
		world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5,
				inventory.extractItem(WEAR_PLATE_SLOT, 1, false)));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return (T) this;
		return super.getCapability(capability, facing);
	}
}
