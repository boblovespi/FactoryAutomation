package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.NBTHelper;
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

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;
import static boblovespi.factoryautomation.common.util.TEHelper.IsMechanicalFace;

/**
 * Created by Willi on 5/6/2018.
 * gearbox, like rotarycraft
 */
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
		return getBlockState().get(Gearbox.FACING) == side.getOpposite();
	}

	public boolean SideIsOutput(Direction side)
	{
		return getBlockState().get(Gearbox.FACING) == side;
	}

	@Override
	public boolean HasConnectionOnSide(Direction side)
	{
		return side.getAxis() == getBlockState().get(Gearbox.FACING).getAxis();
	}

	@Override
	public float GetSpeedOnFace(Direction side)
	{
		ForceUpdate(false);
		return SideIsOutput(side) ? speedOut : SideIsInput(side) ? speedIn : 0;
	}

	@Override
	public float GetTorqueOnFace(Direction side)
	{
		ForceUpdate(false);
		return SideIsOutput(side) ? torqueOut : SideIsInput(side) ? torqueIn : 0;
	}

	@Override
	public void SetSpeedOnFace(Direction side, float speed)
	{
		if (SideIsInput(side))
			this.speedIn = speed;
		ForceUpdate(false);
	}

	@Override
	public void SetTorqueOnFace(Direction side, float torque)
	{
		if (SideIsInput(side))
			this.torqueIn = torque;
		ForceUpdate(false);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound)
	{
		speedIn = compound.getFloat("speedIn");
		torqueIn = compound.getFloat("torqueIn");
		speedOut = compound.getFloat("speedOut");
		torqueOut = compound.getFloat("torqueOut");

		inputGear = NBTHelper.GetGear(compound, "gearIn");
		outputGear = NBTHelper.GetGear(compound, "gearOut");

		inputDurability = compound.getInt("inputDurability");
		outputDurability = compound.getInt("outputDurability");

		super.read(state, compound);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putFloat("speedIn", speedIn);
		compound.putFloat("torqueIn", torqueIn);
		compound.putFloat("speedOut", speedOut);
		compound.putFloat("torqueOut", torqueOut);

		NBTHelper.SetGear(compound, "gearIn", inputGear);
		NBTHelper.SetGear(compound, "gearOut", outputGear);

		compound.putInt("inputDurability", inputDurability);
		compound.putInt("outputDurability", outputDurability);

		return super.write(compound);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (world.isRemote)
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
		if (world.isRemote)
		{
			ProcessVisualChanges();
			return;
		}

		BlockState state = getBlockState();
		Direction facing = state.get(Gearbox.FACING);

		TileEntity te = world.getTileEntity(pos.offset(facing.getOpposite()));

		if (IsMechanicalFace(te, facing))
		{
			IMechanicalUser input = GetUser(te, facing);
			torqueIn = input.GetTorqueOnFace(facing);
			speedIn = input.GetSpeedOnFace(facing);

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

			markDirty();

			/* IMPORTANT */
			BlockState state2 = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state2, state2, 3);
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

			markDirty();
			BlockState state2 = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state2, state2, 3);
			return true;

		} else if (outputGear == null)
		{
			outputGear = gear;
			outputDurability = durability;

			markDirty();
			BlockState state2 = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state2, state2, 3);
			return true;
		}
		return false;
	}

	public void RemoveGear()
	{
		if (outputGear != null)
		{
			if (!world.isRemote)
			{
				ItemStack stack = new ItemStack(FAItems.gear.GetItem(outputGear), 1);
				stack.setDamage(outputGear.durability - outputDurability);
				world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack));
			}

			outputGear = null;
			outputDurability = -1;

			markDirty();
			BlockState state2 = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state2, state2, 3);
		} else if (inputGear != null)
		{
			if (!world.isRemote)
			{
				ItemStack stack = new ItemStack(FAItems.gear.GetItem(inputGear), 1);
				stack.setDamage(inputGear.durability - inputDurability);
				world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack));
			}

			inputGear = null;
			inputDurability = -1;

			markDirty();
			BlockState state2 = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state2, state2, 3);
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

		markDirty();
		BlockState state2 = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state2, state2, 3);
	}

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
		read(getBlockState(), pkt.getNbtCompound());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);
		return new SUpdateTileEntityPacket(pos, 0, nbt);
	}
}
