package boblovespi.factoryautomation.api.heat;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

/**
 * Created by Willi on 10/28/2018.
 */
public class CapabilityHeatUser
{
	@CapabilityInject(IHeatUser.class)
	public static Capability<IHeatUser> HEAT_USER_CAPABILITY = null;

	public static void Register()
	{
		CapabilityManager.INSTANCE.register(IHeatUser.class, new Capability.IStorage<IHeatUser>()
		{
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IHeatUser> capability, IHeatUser instance, EnumFacing side)
			{
				NBTTagCompound nbtBase = new NBTTagCompound();

				return nbtBase;
			}

			@Override
			public void readNBT(Capability<IHeatUser> capability, IHeatUser instance, EnumFacing side, NBTBase nbt)
			{
				NBTTagCompound compound = (NBTTagCompound) nbt;
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
