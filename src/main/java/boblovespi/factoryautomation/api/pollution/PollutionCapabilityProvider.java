package boblovespi.factoryautomation.api.pollution;

import boblovespi.factoryautomation.common.config.ConfigFields;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

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

		cap.SetPollutionCallback(n -> {
			World world = chunk.getLevel();
			if (world.isClientSide)
				return;
			int x = chunk.getPos().x;
			int z = chunk.getPos().z;
			LazyOptional<IPollutedChunk> north = world.getChunk(x + 1, z)
													  .getCapability(POLLUTED_CHUNK_CAPABILITY, null);
			LazyOptional<IPollutedChunk> south = world.getChunk(x - 1, z)
													  .getCapability(POLLUTED_CHUNK_CAPABILITY, null);
			LazyOptional<IPollutedChunk> east = world.getChunk(x, z + 1).getCapability(POLLUTED_CHUNK_CAPABILITY, null);
			LazyOptional<IPollutedChunk> west = world.getChunk(x, z - 1).getCapability(POLLUTED_CHUNK_CAPABILITY, null);
			double v = n.GetPollution() - ConfigFields.pollutionCat.spillover;

			north.ifPresent(o -> {
				if (o.GetPollution() < v)
				{
					o.AddPollution(1);
					n.AddPollution(-1);
				}
			});
			south.ifPresent(o -> {
				if (o.GetPollution() < v)
				{
					o.AddPollution(1);
					n.AddPollution(-1);
				}
			});
			east.ifPresent(o -> {
				if (o.GetPollution() < v)
				{
					o.AddPollution(1);
					n.AddPollution(-1);
				}
			});
			west.ifPresent(o -> {
				if (o.GetPollution() < v)
				{
					o.AddPollution(1);
					n.AddPollution(-1);
				}
			});

		});
	}

	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		//noinspection unchecked
		return capability == POLLUTED_CHUNK_CAPABILITY ? LazyOptional.of(() -> (T) cap) : null;
	}
}
