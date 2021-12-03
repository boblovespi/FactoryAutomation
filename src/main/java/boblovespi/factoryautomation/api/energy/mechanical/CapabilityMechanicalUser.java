package boblovespi.factoryautomation.api.energy.mechanical;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

/**
 * Created by Willi on 5/18/2018.
 * a class that does something
 */
public class CapabilityMechanicalUser
{
	@CapabilityInject(IMechanicalUser.class)
	public static Capability<IMechanicalUser> MECHANICAL_USER_CAPABILITY = null;

	public static void Register()
	{
		CapabilityManager.INSTANCE.register(IMechanicalUser.class, new Capability.IStorage<IMechanicalUser>()
		{
			@Nullable
			@Override
			public Tag writeNBT(Capability<IMechanicalUser> capability, IMechanicalUser instance, Direction side)
			{
				CompoundTag nbtBase = new CompoundTag();

				return nbtBase;
			}

			@Override
			public void readNBT(Capability<IMechanicalUser> capability, IMechanicalUser instance, Direction side,
					Tag nbt)
			{
				CompoundTag compound = (CompoundTag) nbt;
				if (instance instanceof MechanicalUser)
				{
					Direction facing = Direction.byName(compound.getString("facing"));
					float speed = compound.getFloat("speed");
					float torque = compound.getFloat("torque");

					instance.SetSpeedOnFace(facing, speed);
					instance.SetTorqueOnFace(facing, torque);

				} else
				{
					throw new RuntimeException("Capability was not instance of default implementation");
				}
			}
		}, MechanicalUser::new);

	}
}
