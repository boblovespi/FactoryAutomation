package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.util.TEHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.Objects;

import static boblovespi.factoryautomation.common.block.fluid.Pump.FACING;

/**
 * Created by Willi on 12/9/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unchecked")
public class TEPump extends TileEntity implements ITickableTileEntity
{
	private static final float transferSpeed = 1f; // amount to decrease transfer time by per 10 rot speed
	private static final float transferAmountScalar = 1f; // transfer amount per 10 rot torque
	protected static int transferTime = 80;
	protected static int transferAmount = 1500;
	private float timer;
	private final MechanicalUser mechanicalUser;
	private boolean firstTick = true;

	public TEPump()
	{
		super(TileEntityHandler.tePump);
		this.timer = 0;
		mechanicalUser = new MechanicalUser();
	}

	@Override
	public void tick()
	{
		if (level.isClientSide)
			return;

		if (firstTick)
			FirstLoad();

		timer -= transferSpeed * mechanicalUser.GetSpeed() / 10f;
		Direction dir = Objects.requireNonNull(level).getBlockState(worldPosition).getValue(FACING);
		if (timer < 0)
		{
			TileEntity pushTo = level.getBlockEntity(worldPosition.relative(dir.getOpposite()));
			TileEntity takeFrom = level.getBlockEntity(worldPosition.relative(dir));

			if (pushTo != null && takeFrom != null)
			{
				LazyOptional<IFluidHandler> takeFromCapability = takeFrom
						.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir.getOpposite());
				LazyOptional<IFluidHandler> pushToCapability = pushTo
						.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir);

				if (takeFromCapability.isPresent() && pushToCapability.isPresent())
				{
					FluidStack drain;
					//noinspection ConstantConditions
					drain = takeFromCapability.orElse(null).drain(
							(int) (transferAmount * transferAmountScalar * mechanicalUser.GetTorque() / 10f),
							IFluidHandler.FluidAction.EXECUTE);

					pushToCapability.ifPresent(n -> takeFromCapability.ifPresent(m -> m.drain(
							n.fill(drain.copy(), IFluidHandler.FluidAction.EXECUTE),
							IFluidHandler.FluidAction.EXECUTE)));

				}
			}
			timer = transferTime;
		}

		boolean hasConnection = false;
		for (Direction facing : mechanicalUser.GetSides())
		{
			Direction opposite = facing.getOpposite();
			TileEntity te = level.getBlockEntity(worldPosition.relative(facing));
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
			mechanicalUser.SetTorqueOnFace(Direction.from3DDataValue((dir.get3DDataValue() + 2) % 6), 0);
			mechanicalUser.SetSpeedOnFace(Direction.from3DDataValue((dir.get3DDataValue() + 2) % 6), 0);
		}
		setChanged();
	}

	public void FirstLoad()
	{
		Direction dir = Objects.requireNonNull(level).getBlockState(worldPosition).getValue(FACING);
		mechanicalUser.SetSides(EnumSet.complementOf(EnumSet.of(dir, dir.getOpposite())));
		firstTick = false;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY && facing != null && mechanicalUser
				.GetSides().contains(facing))
			return LazyOptional.of(() -> (T) mechanicalUser);
		return super.getCapability(capability, facing);
	}

	@Override
	public void load(BlockState state, CompoundNBT tag)
	{
		super.load(state, tag);
		timer = tag.getFloat("timer");
		mechanicalUser.ReadFromNBT(tag.getCompound("mechanicalUser"));
	}

	@Override
	public CompoundNBT save(CompoundNBT tag)
	{
		tag.putFloat("timer", timer);
		tag.put("mechanicalUser", mechanicalUser.WriteToNBT());
		return super.save(tag);
	}
}
