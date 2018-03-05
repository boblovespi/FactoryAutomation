package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.util.capability.IMechanicalUser;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Created by Willi on 2/20/2018.
 */
public class TEHelper
{
	public static boolean IsMechanicalFace(TileEntity te, EnumFacing face)
	{
		return te != null && te instanceof IMechanicalUser
				&& ((IMechanicalUser) te).HasConnectionOnSide(face);
	}
}
