package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.EnergyConnection;
import boblovespi.factoryautomation.api.energy.IRequiresEnergy;
import boblovespi.factoryautomation.common.block.machine.Motor;
import boblovespi.factoryautomation.api.mechanical.IMechanicalUser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

/**
 * Created by Willi on 3/19/2018.
 */
public class TEMotor extends TileEntity implements IMechanicalUser, IRequiresEnergy, ITickable
{
	public float rotation = 0;
	private float speed;
	private float torque;
	private boolean hasTicked = false;
	private int cooldown = -1;
	private float energyProvided;

	@Override
	public boolean HasConnectionOnSide(EnumFacing side)
	{
		return side == world.getBlockState(pos).getValue(Motor.FACING);
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

	@Override
	public boolean IsActive()
	{
		return true;
	}

	@Override
	public TileEntity GetTe()
	{
		return this;
	}

	/**
	 * Add a connection to the machine
	 *
	 * @param connection The {@link EnergyConnection} to add to the machine
	 */
	@Override
	public void AddConnection(EnergyConnection connection)
	{

	}

	/**
	 * Notify the machine that it needs to check all of its connections to see if they still exist
	 */
	@Override
	public void CheckConnections()
	{

	}

	@Override
	public boolean NeedsEnergy()
	{
		return IsActive();
	}

	/**
	 * @return The amount of energy the machine requires, not including the amount of energy already inserted
	 */
	@Override
	public float AmountNeeded()
	{
		return NeedsEnergy() ? 20 : 0;
	}

	/**
	 * @return The amount of energy the machine requires minus the amount of energy already inserted
	 */
	@Override
	public float ActualAmountNeeded()
	{
		return MathHelper.clamp(AmountNeeded() - energyProvided, 0, AmountNeeded());
	}

	/**
	 * Insert energy into a machine that needs energy
	 *
	 * @param amount   The amount of energy to insert
	 * @param simulate Whether or not to simulate the insertion; ie. if {@code simulate == true}, then no energy will actually be insertion
	 * @return Whether or not the energy was consumed by the machine
	 */
	@Override
	public boolean InsertEnergy(float amount, boolean simulate)
	{
		ForceUpdate(simulate);

		if (!simulate)
			energyProvided += amount;

		return true;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if (world.isRemote)
		{
			rotation = (rotation + speed) % 360;
			return;
		}

		hasTicked = false;
		if ((cooldown = ++cooldown % 20) == 0)
			ForceUpdate(true);
	}

	private void ForceUpdate(boolean changeEnergy)
	{
		if (world.isRemote)
			return;
		if (hasTicked)
			return;
		hasTicked = true;

		if (changeEnergy)
			energyProvided = 0;

		UpdateMotor();

		markDirty();
		IBlockState state2 = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state2, state2, 3);
	}

	private void UpdateMotor()
	{
		speed = energyProvided * 0.2f;
		torque = energyProvided * 0.2f;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		energyProvided = compound.getFloat("energyProvided");
		speed = compound.getFloat("speed");
		torque = compound.getFloat("torque");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setFloat("energyProvided", energyProvided);
		compound.setFloat("speed", speed);
		compound.setFloat("torque", torque);
		return compound;
	}

	/**
	 * Called when you receive a TileEntityData packet for the location this
	 * TileEntity is currently in. On the client, the NetworkManager will always
	 * be the remote server. On the server, it will be whomever is responsible for
	 * sending the packet.
	 *
	 * @param net The NetworkManager the packet originated from
	 * @param pkt The data packet
	 */
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
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
}
