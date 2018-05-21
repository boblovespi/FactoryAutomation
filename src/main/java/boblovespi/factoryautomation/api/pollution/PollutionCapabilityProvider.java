package boblovespi.factoryautomation.api.pollution;

import boblovespi.factoryautomation.common.config.ConfigFields;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static boblovespi.factoryautomation.api.pollution.CapabilityPollutedChunk.POLLUTED_CHUNK_CAPABILITY;

/**
 * Created by Willi on 5/20/2018.
 * <p>
 * a class that does stuff
 */
public class PollutionCapabilityProvider implements ICapabilityProvider
{
	private final Chunk chunk;
	private PollutedChunk cap;

	public PollutionCapabilityProvider(Chunk chunk)
	{
		this.chunk = chunk;
		this.cap = new PollutedChunk();

		cap.SetPollutionCallback(n ->
		{
			World world = chunk.getWorld();
			if (world.isRemote)
				return;
			int x = chunk.x;
			int z = chunk.z;
			IPollutedChunk north = world.getChunkFromChunkCoords(x + 1, z)
										.getCapability(POLLUTED_CHUNK_CAPABILITY, null);
			IPollutedChunk south = world.getChunkFromChunkCoords(x - 1, z)
										.getCapability(POLLUTED_CHUNK_CAPABILITY, null);
			IPollutedChunk east = world.getChunkFromChunkCoords(x, z + 1)
									   .getCapability(POLLUTED_CHUNK_CAPABILITY, null);
			IPollutedChunk west = world.getChunkFromChunkCoords(x, z - 1)
									   .getCapability(POLLUTED_CHUNK_CAPABILITY, null);
			double v = n.GetPollution() - ConfigFields.pollutionCat.spillover;

			if (north != null && north.GetPollution() < v)
			{
				north.AddPollution(1);
				n.AddPollution(-1);
			}
			if (south != null && south.GetPollution() < v)
			{
				south.AddPollution(1);
				n.AddPollution(-1);
			}
			if (east != null && east.GetPollution() < v)
			{
				east.AddPollution(1);
				n.AddPollution(-1);
			}
			if (west != null && west.GetPollution() < v)
			{
				west.AddPollution(1);
				n.AddPollution(-1);
			}

		});
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == POLLUTED_CHUNK_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		//noinspection unchecked
		return capability == POLLUTED_CHUNK_CAPABILITY ? (T) cap : null;
	}
}
