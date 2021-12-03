package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
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
	public void load(BlockState state, CompoundTag tag)
	{
		super.load(state, tag);
		speed = tag.getFloat("speed");
		torque = tag.getFloat("torque");
	}

	@Override
	public CompoundTag save(CompoundTag tag)
	{
		tag.putFloat("speed", speed);
		tag.putFloat("torque", torque);
		return super.save(tag);
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
