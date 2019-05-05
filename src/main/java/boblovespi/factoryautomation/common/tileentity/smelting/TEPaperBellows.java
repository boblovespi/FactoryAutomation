package boblovespi.factoryautomation.common.tileentity.smelting;

import boblovespi.factoryautomation.api.misc.CapabilityBellowsUser;
import boblovespi.factoryautomation.api.misc.IBellowsable;
import boblovespi.factoryautomation.common.block.processing.PaperBellows;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Created by Willi on 5/5/2019.
 */
public class TEPaperBellows extends TileEntity
{
	public void Blow()
	{
		EnumFacing facing = world.getBlockState(pos).getValue(PaperBellows.FACING);
		TileEntity te = world.getTileEntity(pos.offset(facing));
		IBellowsable capability = te.getCapability(CapabilityBellowsUser.BELLOWS_USER_CAPABILITY, facing.getOpposite());
		if (capability != null)
			capability.Blow(0.75f, 400);
	}
}
