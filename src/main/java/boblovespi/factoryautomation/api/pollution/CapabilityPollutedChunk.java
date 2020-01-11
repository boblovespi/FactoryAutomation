package boblovespi.factoryautomation.api.pollution;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

/**
 * Created by Willi on 5/18/2018.
 * a class that does something
 */
public class CapabilityPollutedChunk
{
	@CapabilityInject(IPollutedChunk.class)
	public static Capability<IPollutedChunk> POLLUTED_CHUNK_CAPABILITY = null;

	public static void Register()
	{
		CapabilityManager.INSTANCE.register(IPollutedChunk.class, new Capability.IStorage<IPollutedChunk>()
		{
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IPollutedChunk> capability, IPollutedChunk instance, Direction side)
			{
				CompoundNBT nbtBase = new CompoundNBT();
				nbtBase.setFloat("pollution", instance.GetPollution());
				return nbtBase;
			}

			@Override
			public void readNBT(Capability<IPollutedChunk> capability, IPollutedChunk instance, Direction side,
					NBTBase nbt)
			{
				CompoundNBT compound = (CompoundNBT) nbt;
				if (instance instanceof PollutedChunk)
				{
					((PollutedChunk) instance).SetPollution(compound.getFloat("pollution"));
				} else
				{
					throw new RuntimeException("Capability was not instance of default implementation");
				}
			}
		}, PollutedChunk::new);

	}
}
