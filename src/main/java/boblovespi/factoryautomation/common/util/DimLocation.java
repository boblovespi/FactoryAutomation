package boblovespi.factoryautomation.common.util;

import net.minecraft.util.math.BlockPos;

/**
 * Created by Willi on 12/19/2017.
 */
public class DimLocation
{
	public static final int CLASS_KEY = 31415;

	private int dim, x, y, z;

	public DimLocation(int dim, int x, int y, int z)
	{
		this.dim = dim;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockPos getPos()
	{
		return new BlockPos(x, y, z);
	}

	public int getDim()
	{
		return dim;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getZ()
	{
		return z;
	}
}

