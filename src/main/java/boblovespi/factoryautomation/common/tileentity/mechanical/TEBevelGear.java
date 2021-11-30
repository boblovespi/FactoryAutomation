package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.EnergyConstants;
import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.block.mechanical.BevelGear;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;
import static boblovespi.factoryautomation.common.util.TEHelper.IsMechanicalFace;

/**
 * Created by Willi on 6/21/2019.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEBevelGear extends TileEntity implements ITickableTileEntity
{
	public float rotation = 0;
	private final MechanicalUser user;
	private int counter = -1;
	private boolean firstTick = true;

	public TEBevelGear()
	{
		super(TileEntityHandler.teBevelGear);
		user = new MechanicalUser();
	}

	public void FirstLoad()
	{
		BlockState state = level.getBlockState(worldPosition);
		Direction negativeFacing = BevelGear.GetNegative(state);
		Direction positiveFacing = state.getValue(BevelGear.FACING);
		user.SetSides(EnumSet.of(negativeFacing, positiveFacing));
		firstTick = false;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT tag)
	{
		super.load(state, tag);
		user.ReadFromNBT(tag.getCompound("user"));
	}

	@Override
	public CompoundNBT save(CompoundNBT tag)
	{
		tag.put("user", user.WriteToNBT());
		return super.save(tag);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (level.isClientSide)
		{
			rotation += EnergyConstants.RadiansSecondToDegreesTick(user.GetSpeed());
			rotation %= 360;
			return;
		}
		if (firstTick)
			FirstLoad();

		++counter;
		counter %= 4;

		if (counter == 0)
		{
			BlockState state = level.getBlockState(worldPosition);

			Direction negativeFacing = BevelGear.GetNegative(state);
			Direction positiveFacing = state.getValue(BevelGear.FACING);

			TileEntity front = level.getBlockEntity(worldPosition.relative(positiveFacing));
			TileEntity back = level.getBlockEntity(worldPosition.relative(negativeFacing));

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

			setChanged();

			/* IMPORTANT */
			BlockState state2 = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state2, state2, 3);

		}
	}

	public float GetSpeed()
	{
		return EnergyConstants.RadiansSecondToDegreesTick(user.GetSpeed());
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
