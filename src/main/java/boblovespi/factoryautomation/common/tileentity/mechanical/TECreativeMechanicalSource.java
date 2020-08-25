package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

/**
 * Created by Willi on 2/20/2018.
 */
public class TECreativeMechanicalSource extends TileEntity implements IMechanicalUser
{
	private float torque;
	private float speed;

	public TECreativeMechanicalSource()
	{
		super(TileEntityHandler.teCreativeMechanicalSource);
	}

	@Override
	public boolean HasConnectionOnSide(Direction side)
	{
		return true;
	}

	@Override
	public float GetSpeedOnFace(Direction side)
	{
		return speed;
	}

	@Override
	public float GetTorqueOnFace(Direction side)
	{
		return torque;
	}

	@Override
	public void SetSpeedOnFace(Direction side, float speed)
	{

	}

	@Override
	public void SetTorqueOnFace(Direction side, float torque)
	{

	}

	@Override
	public void read(CompoundNBT tag)
	{
		super.read(tag);
		speed = tag.getFloat("speed");
		torque = tag.getFloat("torque");
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		tag.putFloat("speed", speed);
		tag.putFloat("torque", torque);
		return super.write(tag);
	}

	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) this);
		return LazyOptional.empty();
	}
}
