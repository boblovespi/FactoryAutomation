package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static boblovespi.factoryautomation.common.block.fluid.Pump.FACING;

/**
 * Created by Willi on 12/9/2018.
 */
public class TEPump extends TileEntity implements ITickable
{
	private static final float transferSpeed = 1f; // amount to decrease transfer time by per 10 rot speed
	private static final float transferAmountScalar = 1f; // transfer amount per 10 rot torque
	protected static int transferTime = 80;
	protected static int transferAmount = 1500;
	private float timer;
	private MechanicalUser mechanicalUser;

	public TEPump()
	{
		this.timer = 0;
		mechanicalUser = new MechanicalUser();
	}

	@Override
	public void update()
	{
		if (world.isRemote)
			return;

		timer -= transferSpeed * mechanicalUser.GetSpeed() / 10f;
		EnumFacing dir = world.getBlockState(pos).getValue(FACING);
		if (timer < 0)
		{
			TileEntity pushTo = world.getTileEntity(pos.offset(dir.getOpposite()));
			TileEntity takeFrom = world.getTileEntity(pos.offset(dir));

			if (pushTo != null && takeFrom != null)
			{
				IFluidHandler takeFromCapability = takeFrom
						.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir.getOpposite());
				IFluidHandler pushToCapability = pushTo
						.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir);

				if (takeFromCapability != null && pushToCapability != null)
				{
					FluidStack drain = takeFromCapability
							.drain((int) (transferAmount * transferAmountScalar * mechanicalUser.GetTorque() / 10f),
									false);
					if (drain != null)
					{
						int fill = pushToCapability.fill(drain.copy(), true);
						if (fill > 0)
						{
							takeFromCapability.drain(fill, true);
						}
					}
				}
			}
			timer = transferTime;
		}

		boolean hasConnection = false;
		for (EnumFacing facing : mechanicalUser.GetSides())
		{
			EnumFacing opposite = facing.getOpposite();
			TileEntity te = world.getTileEntity(pos.offset(facing));
			if (TEHelper.IsMechanicalFace(te, opposite))
			{
				hasConnection = true;
				IMechanicalUser user = TEHelper.GetUser(te, opposite);
				mechanicalUser.SetSpeedOnFace(facing, user.GetSpeedOnFace(opposite));
				mechanicalUser.SetTorqueOnFace(facing, user.GetTorqueOnFace(opposite));
			}
		}
		if (!hasConnection)
		{
			mechanicalUser.SetTorqueOnFace(EnumFacing.getFront((dir.getIndex() + 2) % 6), 0);
			mechanicalUser.SetSpeedOnFace(EnumFacing.getFront((dir.getIndex() + 2) % 6), 0);
		}
		markDirty();
	}

	@Override
	public void onLoad()
	{
		EnumFacing dir = world.getBlockState(pos).getValue(FACING);
		mechanicalUser.SetSides(EnumSet.complementOf(EnumSet.of(dir, dir.getOpposite())));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY && facing != null && mechanicalUser
				.GetSides().contains(facing))
			return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY && facing != null && mechanicalUser
				.GetSides().contains(facing))
			return (T) mechanicalUser;
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		timer = tag.getFloat("timer");
		mechanicalUser.ReadFromNBT(tag.getCompoundTag("mechanicalUser"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setFloat("timer", timer);
		tag.setTag("mechanicalUser", mechanicalUser.WriteToNBT());
		return super.writeToNBT(tag);
	}
}
