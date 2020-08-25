package boblovespi.factoryautomation.common.tileentity.smelting;

import boblovespi.factoryautomation.api.misc.CapabilityBellowsUser;
import boblovespi.factoryautomation.api.misc.IBellowsable;
import boblovespi.factoryautomation.client.tesr.IBellowsTE;
import boblovespi.factoryautomation.common.block.processing.PaperBellows;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Created by Willi on 5/5/2019.
 */
public class TEPaperBellows extends TileEntity implements ITickableTileEntity, IBellowsTE
{
	private float lerp = 0;

	public TEPaperBellows()
	{
		super(TileEntityHandler.tePaperBellows);
	}

	public void Blow()
	{
		if (!world.isRemote)
		{
			Direction facing = world.getBlockState(pos).get(PaperBellows.FACING);
			TileEntity te = world.getTileEntity(pos.offset(facing));
			if (te == null)
				return;
			LazyOptional<IBellowsable> capability = te
					.getCapability(CapabilityBellowsUser.BELLOWS_USER_CAPABILITY, facing.getOpposite());

			capability.ifPresent(n -> n.Blow(0.75f, 400));
		} else
		{
			lerp = 1;
		}
	}

	@Override
	public float GetLerp()
	{
		return Math.abs(2 * lerp - 1);
	}

	@Override
	public float GetLerpSpeed()
	{
		return (lerp > 0.5f ? -1 : 1) / 80f;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (!world.isRemote)
			return;
		if (lerp > 0)
		{
			lerp -= 1 / 40f;
		} else
			lerp = 0;
	}
}
