package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;

import static boblovespi.factoryautomation.common.util.TEHelper.IsMechanicalFace;

/**
 * Created by Willi on 5/6/2018.
 * gearbox, like rotarycraft
 */
public class TEGearbox extends TileEntity implements IMechanicalUser, ITickable
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

	public boolean SideIsInput(EnumFacing side)
	{
		return world.getBlockState(pos).getValue(Gearbox.FACING) == side.getOpposite();
	}

	public boolean SideIsOutput(EnumFacing side)
	{
		return world.getBlockState(pos).getValue(Gearbox.FACING) == side;
	}

	@Override
	public boolean HasConnectionOnSide(EnumFacing side)
	{
		return side.getAxis() == world.getBlockState(pos).getValue(Gearbox.FACING).getAxis();
	}

	@Override
	public float GetSpeedOnFace(EnumFacing side)
	{
		ForceUpdate(false);
		return SideIsOutput(side) ? speedOut : SideIsInput(side) ? speedIn : 0;
	}

	@Override
	public float GetTorqueOnFace(EnumFacing side)
	{
		ForceUpdate(false);
		return SideIsOutput(side) ? torqueOut : SideIsInput(side) ? torqueIn : 0;
	}

	@Override
	public void SetSpeedOnFace(EnumFacing side, float speed)
	{
		if (SideIsInput(side))
			this.speedIn = speed;
		ForceUpdate(false);
	}

	@Override
	public void SetTorqueOnFace(EnumFacing side, float torque)
	{
		if (SideIsInput(side))
			this.torqueIn = torque;
		ForceUpdate(false);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		speedIn = compound.getFloat("speedIn");
		torqueIn = compound.getFloat("torqueIn");
		speedOut = compound.getFloat("speedOut");
		torqueOut = compound.getFloat("torqueOut");

		inputGear = NBTHelper.GetGear(compound, "gearIn");
		outputGear = NBTHelper.GetGear(compound, "gearOut");

		inputDurability = compound.getInteger("inputDurability");
		outputDurability = compound.getInteger("outputDurability");

		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setFloat("speedIn", speedIn);
		compound.setFloat("torqueIn", torqueIn);
		compound.setFloat("speedOut", speedOut);
		compound.setFloat("torqueOut", torqueOut);

		NBTHelper.SetGear(compound, "gearIn", inputGear);
		NBTHelper.SetGear(compound, "gearOut", outputGear);

		compound.setInteger("inputDurability", inputDurability);
		compound.setInteger("outputDurability", outputDurability);

		return super.writeToNBT(compound);
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		int meta = getBlockMetadata();
		return new SPacketUpdateTileEntity(pos, meta, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		readFromNBT(tag);
	}

	@Override
	public NBTTagCompound getTileData()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
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

		IBlockState state = world.getBlockState(pos);
		EnumFacing facing = state.getValue(Gearbox.FACING);

		TileEntity te = world.getTileEntity(pos.offset(facing.getOpposite()));

		if (IsMechanicalFace(te, facing))
		{
			IMechanicalUser input = (IMechanicalUser) te;
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
			IBlockState state2 = world.getBlockState(pos);
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
			IBlockState state2 = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state2, state2, 3);
			return true;

		} else if (outputGear == null)
		{
			outputGear = gear;
			outputDurability = durability;

			markDirty();
			IBlockState state2 = world.getBlockState(pos);
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
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(),
						new ItemStack(FAItems.gear.GetItem(outputGear), 1, outputGear.durability - outputDurability)));

			outputGear = null;
			outputDurability = -1;

			markDirty();
			IBlockState state2 = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state2, state2, 3);
		} else if (inputGear != null)
		{
			if (!world.isRemote)
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(),
						new ItemStack(FAItems.gear.GetItem(inputGear), 1, inputGear.durability - inputDurability)));

			inputGear = null;
			inputDurability = -1;

			markDirty();
			IBlockState state2 = world.getBlockState(pos);
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
		IBlockState state2 = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state2, state2, 3);
	}
}
