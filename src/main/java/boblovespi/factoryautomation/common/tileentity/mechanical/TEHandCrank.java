package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static boblovespi.factoryautomation.common.block.mechanical.HandCrank.INVERTED;
import static boblovespi.factoryautomation.common.util.SetBlockStateFlags.FORCE_BLOCK_UPDATE;
import static boblovespi.factoryautomation.common.util.SetBlockStateFlags.SEND_TO_CLIENT;

/**
 * Created by Willi on 9/3/2018.
 */
public class TEHandCrank extends TileEntity implements ITickableTileEntity
{

	private static final float SPEED = 1;
	public float rotation = 0;
	private MechanicalUser mechanicalUser;
	private boolean isRotating;
	public boolean inverted = false;

	public TEHandCrank()
	{
		super(TileEntityHandler.teHandCrank);
		mechanicalUser = new MechanicalUser(EnumSet.of(Direction.DOWN));
		isRotating = false;
	}

	@Override
	public void onLoad()
	{
		inverted = getBlockState().get(INVERTED);
		mechanicalUser.SetSides(EnumSet.of(inverted ? Direction.UP : Direction.DOWN));
	}

	public void Rotate()
	{
		if (!isRotating)
		{
			isRotating = true;

			mechanicalUser.SetTorqueOnFace(inverted ? Direction.UP : Direction.DOWN, 1f);
			mechanicalUser.SetSpeedOnFace(inverted ? Direction.UP : Direction.DOWN, 1f);

			markDirty();

			/* IMPORTANT */
			world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), FORCE_BLOCK_UPDATE | SEND_TO_CLIENT);
		}
	}

	@Override
	public void read(CompoundNBT tag)
	{
		super.read(tag);
		mechanicalUser.ReadFromNBT(tag.getCompound("mechanicalUser"));
		rotation = tag.getFloat("rotation");
		isRotating = tag.getBoolean("isRotating");
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		tag.put("mechanicalUser", mechanicalUser.WriteToNBT());
		tag.putFloat("rotation", rotation);
		tag.putBoolean("isRotating", isRotating);
		return super.write(tag);
	}

	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(()->(T) mechanicalUser);
		return null;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (isRotating)
		{
			rotation += SPEED;

			if (rotation >= 360)
			{
				rotation = 0;
				isRotating = false;

				mechanicalUser.SetTorqueOnFace(inverted ? Direction.UP : Direction.DOWN, 0);
				mechanicalUser.SetSpeedOnFace(inverted ? Direction.UP : Direction.DOWN, 0);

				markDirty();

				/* IMPORTANT */
				world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), FORCE_BLOCK_UPDATE | SEND_TO_CLIENT);
			}
		}
	}

	public boolean IsRotating()
	{
		return isRotating;
	}
}
