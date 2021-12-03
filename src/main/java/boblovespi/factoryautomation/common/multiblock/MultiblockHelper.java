package boblovespi.factoryautomation.common.multiblock;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.tileentity.TEMultiblockPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Created by Willi on 2/17/2018.
 */
public class MultiblockHelper
{
	private static boolean IterateOverMultiblock(Level world, BlockPos controllerPos, String structureId,
			Direction facing, IterateAction action)
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
					BlockEntity te = world.getBlockEntity(loc);
					TEMultiblockPart tePart;
					MultiblockPart part = structure.GetPattern()[x][y][z];

					//					System.out.println("loc = " + loc);
					//					System.out.println("relative = ( " + x + " , " + y + " , " + z + " )");
					//					System.out.println("part = " + part.GetBlock().getLocalizedName());
					//					System.out.println("block in world = " + world.getBlockState(loc).getBlock().getLocalizedName());

					if (te != null && te instanceof TEMultiblockPart)
					{
						tePart = (TEMultiblockPart) te;

						if (action == IterateAction.CHECK_VALID)
						{
							if (!tePart.GetStructureId().equals(structureId))
							{
								return false;
							}
						} else if (action == IterateAction.BREAK)
						{
							world.setBlockAndUpdate(loc, tePart.GetBlockState());
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
							if (te instanceof IMultiblockControllerTE)
								continue;
							BlockState state = world.getBlockState(loc);

							if (part.AllowsAnyBlock() && state.getBlock().isAir(state, world, loc))
								continue;

							world.setBlockAndUpdate(loc, FABlocks.multiblockPart.ToBlock().defaultBlockState());
							TEMultiblockPart newPart = (TEMultiblockPart) world.getBlockEntity(loc);

							newPart.SetMultiblockInformation(structureId, new BlockPos(x, y, z),
									AddWithRotation(AddWithRotation(BlockPos.ZERO, x, y, z, facing), -offset[0],
											-offset[1], -offset[2], facing), state);
						}
					}
				}
			}
		}

		return true;
	}

	public static boolean IsStructureComplete(Level world, BlockPos pos, String id, Direction facing)
	{
		return IterateOverMultiblock(world, pos, id, facing, IterateAction.CHECK_VALID);
	}

	public static void CreateStructure(Level world, BlockPos pos, String id, Direction facing)
	{
		IterateOverMultiblock(world, pos, id, facing, IterateAction.CREATE);
	}

	public static void BreakStructure(Level world, BlockPos pos, String id, Direction facing)
	{
		IterateOverMultiblock(world, pos, id, facing, IterateAction.BREAK);
	}

	public static BlockPos AddWithRotation(BlockPos pos, int x, int y, int z, Direction dir)
	{
		switch (dir)
		{
		case NORTH:
			return pos.offset(-z, y, x);
		case SOUTH:
			return pos.offset(z, y, -x);
		case WEST:
			return pos.offset(x, y, z);
		case EAST:
			return pos.offset(-x, y, -z);
		}
		return pos.offset(x, y, z);
	}

	private enum IterateAction
	{
		CREATE, BREAK, CHECK_VALID
	}
}
