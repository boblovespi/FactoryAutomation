package boblovespi.factoryautomation.common.tileentity.mechanical;

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
		BlockState state = world.getBlockState(levelPosition);
		Direction negativeFacing = BevelGear.getNegative(state);
		Direction positiveFacing = state.getValue(BevelGear.FACING);
		user.setSides(EnumSet.of(negativeFacing, positiveFacing));
		firstTick = false;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT tag)
	{
		super.load(state, tag);
		user.loadFromNBT(tag.getCompound("user"));
	}

	@Override
	public CompoundNBT save(CompoundNBT tag)
	{
		tag.put("user", user.saveToNBT());
		return super.save(tag);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (world.isClientSide)
		{
			rotation += user.getSpeed();
			rotation %= 360;
			return;
		}
		if (firstTick)
			FirstLoad();

		++counter;
		counter %= 4;

		if (counter == 0)
		{
			BlockState state = world.getBlockState(levelPosition);

			Direction negativeFacing = BevelGear.getNegative(state);
			Direction positiveFacing = state.getValue(BevelGear.FACING);

			TileEntity front = world.getTileEntity(levelPosition.relative(positiveFacing));
			TileEntity back = world.getTileEntity(levelPosition.relative(negativeFacing));

			user.setSpeedOnFace(negativeFacing, ((IsMechanicalFace(front, positiveFacing.getOpposite()) ?
					GetUser(front, positiveFacing.getOpposite()).getSpeedOnFace(positiveFacing.getOpposite()) : 0) + (
					IsMechanicalFace(back, negativeFacing.getOpposite()) ?
							GetUser(back, negativeFacing.getOpposite()).getSpeedOnFace(negativeFacing.getOpposite()) :
							0)) / 2f);

			user.setTorqueOnFace(negativeFacing, ((IsMechanicalFace(front, positiveFacing.getOpposite()) ?
					GetUser(front, positiveFacing.getOpposite()).getTorqueOnFace(positiveFacing.getOpposite()) : 0) + (
					IsMechanicalFace(back, negativeFacing.getOpposite()) ?
							GetUser(back, negativeFacing.getOpposite()).getTorqueOnFace(negativeFacing.getOpposite()) :
							0)) / 2f);

			setChanged();

			/* IMPORTANT */
			BlockState state2 = world.getBlockState(levelPosition);
			world.sendBlockUpdatedd(levelPosition, state2, state2, 3);

		}
	}

	public float GetSpeed()
	{
		return user.getSpeed();
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
