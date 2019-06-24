package boblovespi.factoryautomation.api.misc;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

/**
 * Created by Willi on 4/27/2019.
 */
public class CapabilityBellowsUser
{
	@CapabilityInject(IBellowsable.class)
	public static Capability<IBellowsable> BELLOWS_USER_CAPABILITY = null;

	public static void Register()
	{
		CapabilityManager.INSTANCE.register(IBellowsable.class, new Capability.IStorage<IBellowsable>()
		{
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IBellowsable> capability, IBellowsable instance, EnumFacing side)
			{
				return new NBTTagCompound();
			}

			@Override
			public void readNBT(Capability<IBellowsable> capability, IBellowsable instance, EnumFacing side,
					NBTBase nbt)
			{

			}
		}, () -> new BellowsUser(0.5f));

	}
}
