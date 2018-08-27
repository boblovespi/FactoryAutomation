package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
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
		setCreativeTab(CreativeTabs.DECORATIONS);
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

		BlockPos down1 = pos.down();
		BlockPos down2 = pos.down(2);
		IBlockState dState1 = world.getBlockState(down1);
		IBlockState dState2 = world.getBlockState(down2);
		BlockPos up1 = pos.up();
		BlockPos up2 = pos.up(2);
		IBlockState uState1 = world.getBlockState(up1);
		IBlockState uState2 = world.getBlockState(up2);
		if (height != 0)
		{
			if (dState1.getBlock() != this && dState2.getBlock() == this && dState2.getValue(HEIGHT) != 0)
			{
				// world.notifyNeighborsOfStateChange();
			}
		} else
		{
		}
		{
			//			if (height == 0)
			//			{
			//				world.setBlockState(pos, state.withProperty(HEIGHT, 1));
			//			} else if (height == 1)
			//			{
			//				IBlockState down1 = world.getBlockState(pos.down());
			//				IBlockState down2 = world.getBlockState(pos.down(2));
			//
			//				if (down1.getBlock() == this && down2.getBlock() == this && down1.getValue(HEIGHT) != 0)
			//				{
			//					world.setBlockState(pos.down(2), getDefaultState().withProperty(HEIGHT, 3));
			//					world.setBlockState(pos.down(1), getDefaultState().withProperty(HEIGHT, 0));
			//					world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 0));
			//				}
			//			} else if (height == 2)
			//			{
			//				IBlockState down1 = world.getBlockState(pos.down());
			//				if (down1.getBlock() == this && down1.getValue(HEIGHT) != 0)
			//				{
			//					IBlockState up = world.getBlockState(pos.up());
			//					int n = 2;
			//					if (up.getBlock() == this && up.getValue(HEIGHT) < 2)
			//						n = 3;
			//					world.setBlockState(pos.down(1), getDefaultState().withProperty(HEIGHT, n));
			//					world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 0));
			//					if (n == 3)
			//						world.setBlockState(pos.up(), getDefaultState().withProperty(HEIGHT, 0));
			//				}
			//			}
			//			IBlockState up1 = world.getBlockState(fromPos);
			//			IBlockState up2 = world.getBlockState(fromPos.up());
			//
			//			if (up1.getBlock() != this && height > 1)
			//			{
			//				world.setBlockState(pos, state.withProperty(HEIGHT, 1));
			//			} else if (up1.getBlock() == this && (up2.getBlock() != this || up2.getValue(HEIGHT) > 1))
			//			{
			//				if (height >= 1)
			//				{
			//					world.setBlockState(pos, state.withProperty(HEIGHT, 2));
			//					world.setBlockState(pos.up(), state.withProperty(HEIGHT, 0));
			//				} else
			//				{
			//					world.notifyNeighborsOfStateChange(pos, this, false);
			//				}
			//			} else if (up1.getBlock() == this && up2.getBlock() == this)
			//			{
			//				if (height != 0)
			//				{
			//					world.setBlockState(pos, state.withProperty(HEIGHT, 3));
			//					world.setBlockState(pos.up(), state.withProperty(HEIGHT, 0));
			//					world.setBlockState(pos.up(1), state.withProperty(HEIGHT, 0));
			//				} else
			//					world.notifyNeighborsOfStateChange(pos, this, false);
			//			}
		}

		// EffectivelyPlace(world, pos);
		UpdateState(world, pos, state);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, HEIGHT);
	}

	/**
	 * Gets the {@link IBlockState} to place
	 */
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getDefaultState().withProperty(HEIGHT, 1);
	}

	private void EffectivelyPlace(World world, BlockPos pos)
	{
		BlockPos down1 = pos.down();
		BlockPos down2 = pos.down(2);
		IBlockState dState1 = world.getBlockState(down1);
		IBlockState dState2 = world.getBlockState(down2);
		BlockPos up1 = pos.up();
		BlockPos up2 = pos.up(2);
		IBlockState uState1 = world.getBlockState(up1);
		IBlockState uState2 = world.getBlockState(up2);

		boolean defaultHeight = true;

		if (dState1.getBlock() == this)
		{
			if (dState1.getValue(HEIGHT) == 1)
			{
				if (uState1.getBlock() == this && uState1.getValue(HEIGHT) == 1)
				{
					world.setBlockState(down1, dState1.withProperty(HEIGHT, 3));
					world.setBlockState(up1, uState1.withProperty(HEIGHT, 0));
					world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 0));
					defaultHeight = false;
				} else
				{
					world.setBlockState(down1, dState1.withProperty(HEIGHT, 2));
					world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 0));
					defaultHeight = false;
				}
			} else if (dState2.getBlock() == this && dState2.getValue(HEIGHT) == 2 && dState1.getValue(HEIGHT) == 0)
			{
				world.setBlockState(down2, dState2.withProperty(HEIGHT, 3));
				world.setBlockState(down1, dState1.withProperty(HEIGHT, 0));
				world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 0));
				defaultHeight = false;
			}
		}
		if (uState1.getBlock() == this && defaultHeight)
		{
			if (uState1.getValue(HEIGHT) == 1)
			{
				world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 2));
				world.setBlockState(up1, uState1.withProperty(HEIGHT, 0));
				defaultHeight = false;
			} else if (uState1.getValue(HEIGHT) == 2 && uState2.getBlock() == this && uState2.getValue(HEIGHT) == 0)
			{
				world.setBlockState(up1, dState2.withProperty(HEIGHT, 0));
				world.setBlockState(up2, dState1.withProperty(HEIGHT, 0));
				world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 3));
				defaultHeight = false;
			}
		}
		if (defaultHeight)
		{
			world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 1));
		}
	}

	private void UpdateState(World world, BlockPos pos, IBlockState state)
	{
		int height = state.getValue(HEIGHT);
		BlockPos down1 = pos.down();
		BlockPos down2 = pos.down(2);
		IBlockState dState1 = world.getBlockState(down1);
		IBlockState dState2 = world.getBlockState(down2);
		BlockPos up1 = pos.up();
		BlockPos up2 = pos.up(2);
		IBlockState uState1 = world.getBlockState(up1);
		IBlockState uState2 = world.getBlockState(up2);

		if (height == 0)
		{
			if (dState1.getBlock() == this)
			{
				UpdateState(world, down1, dState1);
			}
			if (dState2.getBlock() == this)
			{
				UpdateState(world, down2, dState2);
			}
		} else if (uState1.getBlock() == this)
		{
			if (uState1.getValue(HEIGHT) == 0)
			{
				if (uState2.getBlock() == this && uState2.getValue(HEIGHT) < 2)
				{
					world.setBlockState(up1, uState2.withProperty(HEIGHT, 0));
					world.setBlockState(up2, uState1.withProperty(HEIGHT, 0));
					world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 3));
				} else
				{
					world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 2));
					world.setBlockState(up1, uState1.withProperty(HEIGHT, 0));
				}
			} else if (uState1.getValue(HEIGHT) == 1)
			{
				if (uState2.getBlock() == this && uState2.getValue(HEIGHT) == 1)
				{
					world.setBlockState(up1, uState2.withProperty(HEIGHT, 0));
					world.setBlockState(up2, uState1.withProperty(HEIGHT, 0));
					world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 3));
				} else
				{
					world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 2));
					world.setBlockState(up1, uState1.withProperty(HEIGHT, 0));
				}
			} else if (uState1.getValue(HEIGHT) == 2 && uState2.getBlock() == this && uState2.getValue(HEIGHT) == 0)
			{
				world.setBlockState(up1, uState2.withProperty(HEIGHT, 0));
				world.setBlockState(up2, uState1.withProperty(HEIGHT, 0));
				world.setBlockState(pos, getDefaultState().withProperty(HEIGHT, 3));
			}
		} else
		{
			if (height == 3 && uState2.getBlock() == this && uState2.getValue(HEIGHT) == 0)
			{
				UpdateState(world, up2, uState2.withProperty(HEIGHT, 1));
			}
			world.setBlockState(pos, state.withProperty(HEIGHT, 1));
		}
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile Entity is set
	 */
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		// EffectivelyPlace(world, pos);
		UpdateState(world, pos, state);
	}

	/**
	 * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
	 */
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		BlockPos down1 = pos.down();
		BlockPos down2 = pos.down(2);
		IBlockState dState1 = world.getBlockState(down1);
		IBlockState dState2 = world.getBlockState(down2);
		BlockPos up1 = pos.up();
		BlockPos up2 = pos.up(2);
		IBlockState uState1 = world.getBlockState(up1);
		IBlockState uState2 = world.getBlockState(up2);
		if (state.getValue(HEIGHT) == 0)
		{
			if (dState1.getBlock() == this)
			{
				UpdateState(world, down1, dState1);
			}
			if (dState2.getBlock() == this)
			{
				UpdateState(world, down2, dState2);
			}
		} else if (state.getValue(HEIGHT) > 1)
		{
			if (uState1.getBlock() == this)
			{
				world.setBlockState(up1, uState1.withProperty(HEIGHT, 1));
				UpdateState(world, up1, uState1.withProperty(HEIGHT, 1));
			}
			if (uState2.getBlock() == this)
			{
				world.setBlockState(up1, uState1.withProperty(HEIGHT, 1));
				UpdateState(world, up2, uState2.withProperty(HEIGHT, 1));
			}
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	/**
	 * Return true if the block is a normal, solid cube.  This
	 * determines indirect power state, entity ejection from blocks, and a few
	 * others.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 * @return True if the block is a full cube
	 */
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
}
