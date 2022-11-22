package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.EnergyConstants;
import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.block.mechanical.BevelGear;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
public class TEBevelGear extends BlockEntity implements ITickable
{
	public float rotation = 0;
	private final MechanicalUser user;
	private int counter = -1;
	private boolean firstTick = true;

	public TEBevelGear(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teBevelGear.get(), pos, state);
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
	public void load(CompoundTag tag)
	{
		super.load(tag);
		user.ReadFromNBT(tag.getCompound("user"));
	}

	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.put("user", user.WriteToNBT());
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

			BlockEntity front = level.getBlockEntity(worldPosition.relative(positiveFacing));
			BlockEntity back = level.getBlockEntity(worldPosition.relative(negativeFacing));

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
		return LazyOptional.empty();
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
	}
}
