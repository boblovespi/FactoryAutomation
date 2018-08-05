package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Willi on 8/4/2018.
 */
public class Pillar extends FABaseBlock
{
	public static final PropertyInteger HEIGHT = PropertyInteger.create("height", 0, 3);

	public Pillar(String name, Metals metal)
	{
		super(Material.IRON, name);
		setHardness(1f);
		setResistance(10f);
		setHarvestLevel("pickaxe", 1);
		setDefaultState(getStateFromMeta(1));
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "metals/" + RegistryName();
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(HEIGHT, meta);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(HEIGHT);
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 */
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		int height = state.getValue(HEIGHT);

		if (fromPos.up().equals(pos))
		{
			if (height == 0)
			{
				world.setBlockState(pos, state.withProperty(HEIGHT, 1));
			} else if (height == 1)
			{
				IBlockState down1 = world.getBlockState(pos.down());
				IBlockState down2 = world.getBlockState(pos.down(2));

				if (down1.getBlock() == this && down2.getBlock() == this && down1.getValue(HEIGHT) != 0)
				{
					world.setBlockState(pos.down(2), getDefaultState().withProperty(HEIGHT, 3));
					world.setBlockState(pos.down(1), getDefaultState().withProperty(HEIGHT, 0));
					world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 0));
				}
			} else if (height == 2)
			{
				IBlockState down1 = world.getBlockState(pos.down());
				if (down1.getBlock() == this && down1.getValue(HEIGHT) != 0)
				{
					IBlockState up = world.getBlockState(pos.up());
					int n = 2;
					if (up.getBlock() == this && up.getValue(HEIGHT) < 2)
						n = 3;
					world.setBlockState(pos.down(1), getDefaultState().withProperty(HEIGHT, n));
					world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 0));
					if (n == 3)
						world.setBlockState(pos.up(), getDefaultState().withProperty(HEIGHT, 0));
				}
			}
		} else if (fromPos.down().equals(pos))
		{
			IBlockState up1 = world.getBlockState(fromPos);
			IBlockState up2 = world.getBlockState(fromPos.up());

			if (up1.getBlock() != this && height > 1)
			{
				world.setBlockState(pos, state.withProperty(HEIGHT, 1));
			} else if (up1.getBlock() == this && (up2.getBlock() != this || up2.getValue(HEIGHT) > 1))
			{
				if (height >= 1)
				{
					world.setBlockState(pos, state.withProperty(HEIGHT, 2));
					world.setBlockState(pos.up(), state.withProperty(HEIGHT, 0));
				} else
				{
					world.notifyNeighborsOfStateChange(pos, this, false);
				}
			} else if (up1.getBlock() == this && up2.getBlock() == this)
			{
				if (height != 0)
				{
					world.setBlockState(pos, state.withProperty(HEIGHT, 3));
					world.setBlockState(pos.up(), state.withProperty(HEIGHT, 0));
					world.setBlockState(pos.up(1), state.withProperty(HEIGHT, 0));
				} else
					world.notifyNeighborsOfStateChange(pos, this, false);
			}
		}
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, HEIGHT);
	}
}
