package boblovespi.factoryautomation.common.multiblock;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.tileentity.TileEntityMultiblockPart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Willi on 2/17/2018.
 */
public class MultiblockHelper
{
	private static boolean IterateOverMultiblock(World world, BlockPos controllerPos, String structureId,
			EnumFacing facing, IterateAction action)
	{
		boolean isValid = true;

		MultiblockStructurePattern structure = MultiblockHandler.Get(structureId);

		MultiblockPart[][][] pattern = structure.GetPattern();

		int[] offset = structure.GetControllerOffset();

		BlockPos lowerLeftFront = AddWithRotation(controllerPos, -offset[0], -offset[1], -offset[2], facing);

		for (int x = 0; x < pattern.length; x++)
		{
			for (int y = 0; y < pattern[x].length; y++)
			{
				for (int z = 0; z < pattern[x][y].length; z++)
				{
					BlockPos loc = AddWithRotation(lowerLeftFront, x, y, z, facing);
					TileEntity te = world.getTileEntity(loc);
					TileEntityMultiblockPart tePart;
					MultiblockPart part = structure.GetPattern()[x][y][z];

					System.out.println("loc = " + loc);
					System.out.println("relative = ( " + x + " , " + y + " , " + z + " )");
					System.out.println("part = " + part.GetBlock().getLocalizedName());
					System.out.println("block in world = " + world.getBlockState(loc).getBlock().getLocalizedName());

					if (te != null && te instanceof TileEntityMultiblockPart)
					{
						tePart = (TileEntityMultiblockPart) te;

						if (action == IterateAction.CHECK_VALID)
						{
							if (!tePart.GetStructureId().equals(structureId))
							{
								return false;
							}
						} else if (action == IterateAction.BREAK)
						{
							world.setBlockState(loc, tePart.GetBlockState());
						}

					} else
					{
						if (action == IterateAction.CHECK_VALID)
						{
							if (!pattern[x][y][z].MatchesBlockstate(world.getBlockState(loc)))
							{
								return false;
							}

						} else if (action == IterateAction.CREATE)
						{
							IBlockState state = world.getBlockState(loc);

							world.setBlockState(loc, FABlocks.multiblockPart.ToBlock().getDefaultState());
							TileEntityMultiblockPart newPart = (TileEntityMultiblockPart) world.getTileEntity(loc);

							newPart.SetMultiblockInformation(structureId, new BlockPos(x, y, z),
									AddWithRotation(AddWithRotation(BlockPos.ORIGIN, -x, -y, -z, facing), offset[0],
											offset[1], offset[2], facing), state);
						}
					}
				}
			}
		}

		return true;
	}

	public static boolean IsStructureComplete(World world, BlockPos pos, String id, EnumFacing facing)
	{
		return IterateOverMultiblock(world, pos, id, facing, IterateAction.CHECK_VALID);
	}

	private static BlockPos AddWithRotation(BlockPos pos, int x, int y, int z, EnumFacing dir)
	{
		switch (dir)
		{
		case NORTH:
			return pos.add(-z, y, x);
		case SOUTH:
			return pos.add(z, y, -x);
		case WEST:
			return pos.add(x, y, z);
		case EAST:
			return pos.add(-x, y, -z);
		}
		return pos.add(x, y, z);
	}

	private enum IterateAction
	{
		CREATE, BREAK, CHECK_VALID
	}
}
