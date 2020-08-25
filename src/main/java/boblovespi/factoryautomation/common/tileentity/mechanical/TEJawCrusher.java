package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.api.recipe.JawCrusherRecipe;
import boblovespi.factoryautomation.common.block.machine.JawCrusher;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MachineTiers;
import boblovespi.factoryautomation.common.tileentity.TEMachine;
import boblovespi.factoryautomation.common.util.RestrictedSlotItemHandler;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.BitSet;

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;

/**
 * Created by Willi on 2/17/2018.
 */
public class TEJawCrusher extends TEMachine<JawCrusherRecipe> implements IMechanicalUser
{
	private static final int INPUT_SLOT = 0;
	private static final int OUTPUT_SLOT = 1;
	private static float processingScalar = 1; // how fast currentProcessingTime decreases
	private float speed = 0;
	private float torque = 0;
	private ItemStackHandler wearPlate;
	private RestrictedSlotItemHandler inputWrapper;
	private RestrictedSlotItemHandler outputWrapper;

	public TEJawCrusher()
	{
		super(1, TileEntityHandler.teJawCrusher);
		wearPlate = new ItemStackHandler();
		inputWrapper = new RestrictedSlotItemHandler(new BitSet(2)
		{{
			set(0);
		}}, processingInv);
		outputWrapper = new RestrictedSlotItemHandler(new BitSet(2)
		{{
			set(1);
		}}, processingInv);
	}

	@Nonnull
	@Override
	protected String FindRecipeName()
	{
		JawCrusherRecipe tRecipe = JawCrusherRecipe
				.FindRecipe(processingInv.getStackInSlot(INPUT_SLOT), GetMachineTier());
		if (tRecipe == null)
			return "none";
		return tRecipe.name;
	}

	@Nullable
	@Override
	protected JawCrusherRecipe FindRecipe(String recipeName)
	{
		return JawCrusherRecipe.GetRecipe(recipeName);
	}

	@Override
	protected int GetMaxProgress()
	{
		return (int) recipeCache.time;
	}

	@Override
	protected void OnRecipeComplete(String recipe)
	{
		processingInv.insertItem(OUTPUT_SLOT, recipeCache.output.apply((float) Math.random()), false);
		processingInv.extractItem(INPUT_SLOT, 1, false);
	}

	@Override
	protected float GetProgressScalar()
	{
		if (speed > recipeCache.speedReq / 2 && torque > recipeCache.torqueReq)
		{
			return MathHelper.clamp(speed / recipeCache.speedReq, 0.5f, 20) * processingScalar;
		}
		return 0;
	}

	@Override
	public void Update()
	{
		Direction facing = world.getBlockState(pos).get(JawCrusher.FACING).rotateYCCW();
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

		if (recipeName.equals("none") && !processingInv.getStackInSlot(0).isEmpty())
		{
			ItemStack stack = processingInv.extractItem(INPUT_SLOT, 1, false);
			processingInv.insertItem(OUTPUT_SLOT, stack, false);
			if (!stack.isEmpty())
				processingInv.insertItem(INPUT_SLOT, stack, false);
		}
	}

	private int GetMachineTier()
	{
		Item item = wearPlate.getStackInSlot(0).getItem();
		if (item == FAItems.wearPlate.GetItem(MachineTiers.IRON))
			return 0;
		else if (item == FAItems.wearPlate.GetItem(MachineTiers.OBSIDIAN))
			return 1;
		else
			return -1;
	}

	@Override
	public boolean HasConnectionOnSide(Direction side)
	{
		return side == world.getBlockState(pos).get(JawCrusher.FACING).rotateYCCW();
	}

	@Override
	public float GetSpeedOnFace(Direction side)
	{
		return HasConnectionOnSide(side) ? speed : 0;
	}

	@Override
	public float GetTorqueOnFace(Direction side)
	{
		return HasConnectionOnSide(side) ? torque : 0;
	}

	@Override
	public void SetSpeedOnFace(Direction side, float speed)
	{
		if (HasConnectionOnSide(side))
			this.speed = speed;
	}

	@Override
	public void SetTorqueOnFace(Direction side, float torque)
	{
		if (HasConnectionOnSide(side))
			this.torque = torque;
	}

	@Override
	protected void ReadCustomNBT(CompoundNBT tag)
	{
		speed = tag.getFloat("speed");
		torque = tag.getFloat("torque");
		wearPlate.deserializeNBT(tag.getCompound("wearPlate"));
	}

	@Override
	protected void WriteCustomNBT(CompoundNBT tag)
	{
		tag.putFloat("speed", speed);
		tag.putFloat("torque", torque);
		tag.put("wearPlate", wearPlate.serializeNBT());
	}

	public void PlaceWearPlate(ItemStack wearPlate)
	{
		if (FAItems.wearPlate.Contains(wearPlate.getItem()))
		{
			if (this.wearPlate.getStackInSlot(0).isEmpty())
			{
				this.wearPlate.insertItem(0, wearPlate.copy().split(1), false);
				wearPlate.shrink(1);
			}
		}
	}

	public void RemovePlate()
	{
		world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5,
				this.wearPlate.extractItem(0, 1, false)));
	}

	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) this);
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == Direction.UP)
			return LazyOptional.of(() -> (T) inputWrapper);
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == Direction.DOWN)
			return LazyOptional.of(() -> (T) outputWrapper);
		return super.getCapability(capability, facing);
	}
}
