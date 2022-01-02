package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 2/20/2018.
 */
@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TECreativeMechanicalSource extends BlockEntity implements IMechanicalUser
{
	public float torque;
	public float speed;

	public TECreativeMechanicalSource(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teCreativeMechanicalSource, pos, state);
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
	public void load(CompoundTag tag)
	{
		super.load(tag);
		speed = tag.getFloat("speed");
		torque = tag.getFloat("torque");
	}

	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.putFloat("speed", speed);
		tag.putFloat("torque", torque);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) this);
		return LazyOptional.empty();
	}
}
