package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.NBTHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;
import static boblovespi.factoryautomation.common.util.TEHelper.IsMechanicalFace;

/**
 * Created by Willi on 5/6/2018.
 * gearbox, like rotarycraft
 */
@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEGearbox extends TileEntity implements IMechanicalUser, ITickableTileEntity
{
	public float rotationIn = 0;
	public float rotationOut = 0;
	public float rotationTop = 0;
	public float speedTop = 0;
	private float speedIn;
	private float torqueIn;
	private float speedOut;
	private float torqueOut;
	private Gearbox.GearType inputGear = null;
	private Gearbox.GearType outputGear = null;
	private int inputDurability = -1;
	private int outputDurability = -1;
	private int counter = -1;

	public TEGearbox()
	{
		super(TileEntityHandler.teGearbox);
	}

	public boolean SideIsInput(Direction side)
	{
		return getBlockState().getValue(Gearbox.FACING) == side.getOpposite();
	}

	public boolean SideIsOutput(Direction side)
	{
		return getBlockState().getValue(Gearbox.FACING) == side;
	}

	@Override
	public boolean hasConnectionOnSide(Direction side)
	{
		return side.getAxis() == getBlockState().getValue(Gearbox.FACING).getAxis();
	}

	@Override
	public float getSpeedOnFace(Direction side)
	{
		ForceUpdate(false);
		return SideIsOutput(side) ? speedOut : SideIsInput(side) ? speedIn : 0;
	}

	@Override
	public float getTorqueOnFace(Direction side)
	{
		ForceUpdate(false);
		return SideIsOutput(side) ? torqueOut : SideIsInput(side) ? torqueIn : 0;
	}

	@Override
	public void setSpeedOnFace(Direction side, float speed)
	{
		if (SideIsInput(side))
			this.speedIn = speed;
		ForceUpdate(false);
	}

	@Override
	public void setTorqueOnFace(Direction side, float torque)
	{
		if (SideIsInput(side))
			this.torqueIn = torque;
		ForceUpdate(false);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		speedIn = compound.getFloat("speedIn");
		torqueIn = compound.getFloat("torqueIn");
		speedOut = compound.getFloat("speedOut");
		torqueOut = compound.getFloat("torqueOut");

		inputGear = NBTHelper.GetGear(compound, "gearIn");
		outputGear = NBTHelper.GetGear(compound, "gearOut");

		inputDurability = compound.getInt("inputDurability");
		outputDurability = compound.getInt("outputDurability");

		super.load(state, compound);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		compound.putFloat("speedIn", speedIn);
		compound.putFloat("torqueIn", torqueIn);
		compound.putFloat("speedOut", speedOut);
		compound.putFloat("torqueOut", torqueOut);

		NBTHelper.SetGear(compound, "gearIn", inputGear);
		NBTHelper.SetGear(compound, "gearOut", outputGear);

		compound.putInt("inputDurability", inputDurability);
		compound.putInt("outputDurability", outputDurability);

		return super.save(compound);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (Objects.requireNonNull(level).isClientSide)
		{
			ProcessVisualChanges();
			return;
		}

		++counter;
		counter %= 100;

		if (counter == 0)
		{
			ForceUpdate(true);
		}
	}

	public void ForceUpdate(boolean damageGears)
	{
		if (Objects.requireNonNull(level).isClientSide)
		{
			ProcessVisualChanges();
			return;
		}

		BlockState state = getBlockState();
		Direction facing = state.getValue(Gearbox.FACING);

		TileEntity te = level.getBlockEntity(levelPosition.relative(facing.getOpposite()));

		if (IsMechanicalFace(te, facing))
		{
			IMechanicalUser input = GetUser(te, facing);
			torqueIn = input.getTorqueOnFace(facing);
			speedIn = input.getSpeedOnFace(facing);

			if (inputGear == null || outputGear == null)
			{
				speedOut = 0;
				torqueOut = 0;
				return;
			}

			torqueOut = (torqueIn * outputGear.scaleFactor) / inputGear.scaleFactor;
			speedOut = (speedIn * inputGear.scaleFactor) / outputGear.scaleFactor;

			if (damageGears && speedIn > 0 && torqueIn > 0)
			{
				inputDurability--;
				outputDurability--;

				if (inputDurability < 0)
					inputGear = null;
				if (outputDurability < 0)
					outputGear = null;
			}

			setChanged();

			/* IMPORTANT */
			BlockState state2 = level.getBlockState(levelPosition);
			level.sendBlockUpdatedd(levelPosition, state2, state2, 3);
		}
	}

	/**
	 * Adds a gear to the gearbox
	 *
	 * @param gear the gear which is attempted to add
	 * @return whether adding the gear was successful or not
	 */
	public boolean AddGear(Gearbox.GearType gear, int durability)
	{
		if (inputGear == null)
		{
			inputGear = gear;
			inputDurability = durability;

			setChanged();
			BlockState state2 = Objects.requireNonNull(level).getBlockState(levelPosition);
			level.sendBlockUpdatedd(levelPosition, state2, state2, 3);
			return true;

		} else if (outputGear == null)
		{
			outputGear = gear;
			outputDurability = durability;

			setChanged();
			BlockState state2 = Objects.requireNonNull(level).getBlockState(levelPosition);
			level.sendBlockUpdatedd(levelPosition, state2, state2, 3);
			return true;
		}
		return false;
	}

	public void RemoveGear()
	{
		if (outputGear != null)
		{
			if (!Objects.requireNonNull(level).isClientSide)
			{
				ItemStack stack = new ItemStack(FAItems.gear.GetItem(outputGear), 1);
				stack.setDamageValue(outputGear.durability - outputDurability);
				level.addFreshEntity(new ItemEntity(level, levelPosition.getX(), levelPosition.getY(), levelPosition.getZ(), stack));
			}

			outputGear = null;
			outputDurability = -1;

			setChanged();
			BlockState state2 = level.getBlockState(levelPosition);
			level.sendBlockUpdatedd(levelPosition, state2, state2, 3);
		} else if (inputGear != null)
		{
			if (!Objects.requireNonNull(level).isClientSide)
			{
				ItemStack stack = new ItemStack(FAItems.gear.GetItem(inputGear), 1);
				stack.setDamageValue(inputGear.durability - inputDurability);
				level.addFreshEntity(new ItemEntity(level, levelPosition.getX(), levelPosition.getY(), levelPosition.getZ(), stack));
			}

			inputGear = null;
			inputDurability = -1;

			setChanged();
			BlockState state2 = level.getBlockState(levelPosition);
			level.sendBlockUpdatedd(levelPosition, state2, state2, 3);
		}
	}

	public float GetSpeedIn()
	{
		return speedIn;
	}

	public float GetSpeedOut()
	{
		return speedOut;
	}

	public Gearbox.GearType GetGearIn()
	{
		return inputGear;
	}

	public Gearbox.GearType GetGearOut()
	{
		return outputGear;
	}

	public void ProcessVisualChanges()
	{
		rotationIn = (rotationIn + speedIn) % 360;
		rotationOut = (rotationOut + speedOut) % 360;

		if (inputGear != null)
		{
			int gInTop = 20 - inputGear.scaleFactor;
			speedTop = (speedIn * inputGear.scaleFactor) / gInTop;
			rotationTop = (rotationTop + speedTop) % 360;
		}

	}

	public void SwitchGears()
	{
		int tempDur = inputDurability;
		Gearbox.GearType tempGear = inputGear;

		inputDurability = outputDurability;
		inputGear = outputGear;
		outputGear = tempGear;
		outputDurability = tempDur;

		setChanged();
		BlockState state2 = Objects.requireNonNull(level).getBlockState(levelPosition);
		level.sendBlockUpdatedd(levelPosition, state2, state2, 3);
	}

	// Todo: update to use non-null.
	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) this);
		return null;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		load(Objects.requireNonNull(level).getBlockState(levelPosition), pkt.getTag());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		save(nbt);
		return new SUpdateTileEntityPacket(levelPosition, 0, nbt);
	}
}
