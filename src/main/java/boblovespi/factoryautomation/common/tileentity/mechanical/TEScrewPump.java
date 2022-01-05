package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.block.fluid.ScrewPump;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Objects;

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;

public class TEScrewPump extends BlockEntity implements ITickable
{
	private static float timeInSeconds = 5;
	private static float minTorque = 0.9f;
	private static int timeInTicks = (int) (20 * timeInSeconds);

	private int updateCounter = 0;
	private float counter = timeInTicks;
	private MechanicalUser user;

	public TEScrewPump(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teScrewPump, pos, state);
		user = new MechanicalUser(EnumSet.of(Direction.UP));
	}

	@Override
	public void tick()
	{
		if (level.isClientSide)
			return;

		updateCounter++;
		updateCounter %= 4;
		if (updateCounter == 0)
		{
			BlockEntity te1 = level.getBlockEntity(worldPosition.above());
			if (TEHelper.IsMechanicalFace(te1, Direction.DOWN))
			{
				user.SetSpeedOnFace(Direction.UP, GetUser(te1, Direction.DOWN).GetSpeedOnFace(Direction.DOWN));
				user.SetTorqueOnFace(Direction.UP, GetUser(te1, Direction.DOWN).GetTorqueOnFace(Direction.DOWN));
			} else
			{
				user.SetSpeedOnFace(Direction.UP, 0);
				user.SetTorqueOnFace(Direction.UP, 0);
			}
			setChanged();
			BlockState state = Objects.requireNonNull(level).getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 3);
		}

		if (user.GetTorque() > minTorque)
			counter -= user.GetSpeed();
		if (counter < 0)
		{
			int waterSides = 0;
			for (Direction dir : Direction.Plane.HORIZONTAL.stream().toList())
			{
				if (level.getFluidState(worldPosition.relative(dir)).getType() == Fluids.WATER)
					waterSides++;
			}
			if (waterSides >= 2)
			{
				if (!level.getFluidState(worldPosition).is(Fluids.WATER))
					level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ScrewPump.WATERLOGGED, true));
			} else
				level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ScrewPump.WATERLOGGED, false));

			var facing = getBlockState().getValue(ScrewPump.FACING);
			var te = level.getBlockEntity(worldPosition.relative(facing).above());
			if (te != null)
			{
				var handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
				handler.ifPresent(n -> n.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE));
			}

			counter = timeInTicks;
		}
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
		counter = tag.getFloat("counter");
		user.ReadFromNBT(tag.getCompound("user"));
	}

	@Override
	protected void saveAdditional(CompoundTag tag)
	{
		tag.putFloat("counter", counter);
		tag.put("user", user.WriteToNBT());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if (cap == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> user).cast();
		return super.getCapability(cap, side);
	}
}
