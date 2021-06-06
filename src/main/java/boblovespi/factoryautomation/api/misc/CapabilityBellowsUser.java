package boblovespi.factoryautomation.api.misc;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
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

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IBellowsable.class, new Capability.IStorage<IBellowsable>()
		{
			@Nullable
			@Override
			public INBT writeNBT(Capability<IBellowsable> capability, IBellowsable instance, Direction side)
			{
				return new CompoundNBT();
			}

			@Override
			public void readNBT(Capability<IBellowsable> capability, IBellowsable instance, Direction side,
					INBT nbt)
			{

			}
		}, () -> new bellowsUser(0.5f));

	}
}
