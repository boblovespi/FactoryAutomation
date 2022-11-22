package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.EnergyConstants;
import boblovespi.factoryautomation.api.energy.electricity.EnergyConnection_;
import boblovespi.factoryautomation.api.energy.electricity.IRequiresEnergy_;
import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.common.block.machine.Motor;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Created by Willi on 3/19/2018.
 */
@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEMotor extends BlockEntity implements IMechanicalUser, IRequiresEnergy_, ITickable
{
	public float rotation = 0;
	private float speed;
	private float torque;
	private boolean hasTicked = false;
	private int cooldown = -1;
	private float energyProvided;

	public TEMotor(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teMotor.get(), pos, state);
	}

	@Override
	public boolean HasConnectionOnSide(Direction side)
	{
		return side == getBlockState().getValue(Motor.FACING);
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
	public boolean IsActive()
	{
		return true;
	}

	@Override
	public BlockEntity GetTe()
	{
		return this;
	}

	/**
	 * Add a connection to the machine
	 *
	 * @param connection The {@link EnergyConnection_} to add to the machine
	 */
	@Override
	public void AddConnection(EnergyConnection_ connection)
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
		return Mth.clamp(AmountNeeded() - energyProvided, 0, AmountNeeded());
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
	public void tick()
	{
		if (Objects.requireNonNull(level).isClientSide)
		{
			rotation = (rotation + EnergyConstants.RadiansSecondToDegreesTick(speed)) % 360;
			return;
		}

		hasTicked = false;
		if ((cooldown = ++cooldown % 20) == 0)
			ForceUpdate(true);
	}

	private void ForceUpdate(boolean changeEnergy)
	{
		if (Objects.requireNonNull(level).isClientSide)
			return;
		if (hasTicked)
			return;
		hasTicked = true;

		if (changeEnergy)
			energyProvided = 0;

		UpdateMotor();

		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
	}

	private void UpdateMotor()
	{
		speed = energyProvided * 0.2f;
		torque = energyProvided * 0.2f;
	}

	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		energyProvided = compound.getFloat("energyProvided");
		speed = compound.getFloat("speed");
		torque = compound.getFloat("torque");
	}

	@Override
	public void saveAdditional(CompoundTag compound)
	{
		compound.putFloat("energyProvided", energyProvided);
		compound.putFloat("speed", speed);
		compound.putFloat("torque", torque);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) this);
		return super.getCapability(capability, facing);
	}
}
