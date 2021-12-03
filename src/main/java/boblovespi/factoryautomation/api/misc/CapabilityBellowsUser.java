package boblovespi.factoryautomation.api.misc;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
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
			public Tag writeNBT(Capability<IBellowsable> capability, IBellowsable instance, Direction side)
			{
				return new CompoundTag();
			}

			@Override
			public void readNBT(Capability<IBellowsable> capability, IBellowsable instance, Direction side,
					Tag nbt)
			{

			}
		}, () -> new BellowsUser(0.5f));

	}
}
