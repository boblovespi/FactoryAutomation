package boblovespi.factoryautomation.api.energy.heat;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
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
			public INBT writeNBT(Capability<IHeatUser> capability, IHeatUser instance, Direction side)
			{
				CompoundNBT nbtBase = new CompoundNBT();

				return nbtBase;
			}

			@Override
			public void readNBT(Capability<IHeatUser> capability, IHeatUser instance, Direction side, INBT nbt)
			{
				CompoundNBT compound = (CompoundNBT) nbt;
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
