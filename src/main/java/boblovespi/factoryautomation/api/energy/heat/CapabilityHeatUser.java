package boblovespi.factoryautomation.api.energy.heat;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

/**
 * Created by Willi on 10/28/2018.
 */
public class CapabilityHeatUser
{
	public static final float AIR_CONDUCTIVITY = 0.024f;
	@CapabilityInject(IHeatUser.class)
	public static Capability<IHeatUser> HEAT_USER_CAPABILITY = null;

	public static void Register()
	{
		CapabilityManager.INSTANCE.register(IHeatUser.class, new Capability.IStorage<IHeatUser>()
		{
			@Nullable
			@Override
			public Tag writeNBT(Capability<IHeatUser> capability, IHeatUser instance, Direction side)
			{
				CompoundTag nbtBase = new CompoundTag();

				return nbtBase;
			}

			@Override
			public void readNBT(Capability<IHeatUser> capability, IHeatUser instance, Direction side, Tag nbt)
			{
				CompoundTag compound = (CompoundTag) nbt;
				if (instance instanceof HeatUser)
				{
					((HeatUser) instance).ReadFromNBT(compound);

				} else
				{
					throw new RuntimeException("Capability was not instance of default implementation");
				}
			}
		}, HeatUser::new);

	}
}
