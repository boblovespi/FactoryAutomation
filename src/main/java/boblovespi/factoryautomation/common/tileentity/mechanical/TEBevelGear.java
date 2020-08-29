package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.block.mechanical.BevelGear;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;
import static boblovespi.factoryautomation.common.util.TEHelper.IsMechanicalFace;

/**
 * Created by Willi on 6/21/2019.
 */
public class TEBevelGear extends TileEntity implements ITickableTileEntity
{
	public float rotation = 0;
	private MechanicalUser user;
	private int counter = -1;
	private boolean firstTick = true;

	public TEBevelGear()
	{
		super(TileEntityHandler.teBevelGear);
		user = new MechanicalUser();
	}

	public void FirstLoad()
	{
		BlockState state = world.getBlockState(pos);
		Direction negativeFacing = BevelGear.GetNegative(state);
		Direction positiveFacing = state.get(BevelGear.FACING);
		user.SetSides(EnumSet.of(negativeFacing, positiveFacing));
		firstTick = false;
	}

	@Override
	public void read(CompoundNBT tag)
	{
		super.read(tag);
		user.ReadFromNBT(tag.getCompound("user"));
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		tag.put("user", user.WriteToNBT());
		return super.write(tag);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (world.isRemote)
		{
			rotation += user.GetSpeed();
			rotation %= 360;
			return;
		}
		if (firstTick)
			FirstLoad();

		++counter;
		counter %= 4;

		if (counter == 0)
		{
			BlockState state = world.getBlockState(pos);

			Direction negativeFacing = BevelGear.GetNegative(state);
			Direction positiveFacing = state.get(BevelGear.FACING);

			TileEntity front = world.getTileEntity(pos.offset(positiveFacing));
			TileEntity back = world.getTileEntity(pos.offset(negativeFacing));

			user.SetSpeedOnFace(negativeFacing, ((IsMechanicalFace(front, positiveFacing.getOpposite()) ?
					GetUser(front, positiveFacing.getOpposite()).GetSpeedOnFace(positiveFacing.getOpposite()) : 0) + (
					IsMechanicalFace(back, negativeFacing.getOpposite()) ?
							GetUser(back, negativeFacing.getOpposite()).GetSpeedOnFace(negativeFacing.getOpposite()) :
							0)) / 2f);

			user.SetTorqueOnFace(negativeFacing, ((IsMechanicalFace(front, positiveFacing.getOpposite()) ?
					GetUser(front, positiveFacing.getOpposite()).GetTorqueOnFace(positiveFacing.getOpposite()) : 0) + (
					IsMechanicalFace(back, negativeFacing.getOpposite()) ?
							GetUser(back, negativeFacing.getOpposite()).GetTorqueOnFace(negativeFacing.getOpposite()) :
							0)) / 2f);

			markDirty();

			/* IMPORTANT */
			BlockState state2 = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state2, state2, 3);

		}
	}

	public float GetSpeed()
	{
		return user.GetSpeed();
	}

	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) user);
		return null;
	}
}
