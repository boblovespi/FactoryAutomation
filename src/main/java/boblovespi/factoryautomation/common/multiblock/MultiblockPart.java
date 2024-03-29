package boblovespi.factoryautomation.common.multiblock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Predicate;

/**
 * Created by Willi on 12/24/2017.
 */
public class MultiblockPart
{
	public static final MultiblockPart EMPTY = new MultiblockPart(Blocks.AIR, false);

	private final Block baseBlock;
	private final Predicate<CompoundTag> blockTagPredicate;
	private final Predicate<BlockState> statePredicate;
	private final boolean mustBeAirBlock;

	public MultiblockPart(Block baseBlock, Predicate<CompoundTag> blockTagPredicate,
			Predicate<BlockState> statePredicate, boolean mustBeAirBlock)
	{
		this.baseBlock = baseBlock;
		this.blockTagPredicate = blockTagPredicate;
		this.statePredicate = statePredicate;
		this.mustBeAirBlock = mustBeAirBlock;
	}

	public MultiblockPart(Block baseBlock, boolean mustBeAirBlock)
	{
		this(baseBlock, compoundNBT -> true, blockState -> true, mustBeAirBlock);
	}

	public MultiblockPart(Block baseBlock)
	{
		this(baseBlock, compoundNBT -> true, blockState -> true, true);
	}

	public static MultiblockPart[][][] FromBlocks(Block[][][] blocks)
	{
		MultiblockPart[][][] parts = new MultiblockPart[blocks.length][blocks[0].length][blocks[0][0].length];

		for (int x = 0; x < blocks.length; x++)
		{
			for (int y = 0; y < blocks[x].length; y++)
			{
				for (int z = 0; z < blocks[x][y].length; z++)
				{
					parts[x][y][z] = new MultiblockPart(blocks[x][y][z], true);
				}
			}
		}

		return parts;
	}

	public Block GetBlock()
	{
		return baseBlock;
	}

	public boolean MatchesNBT(CompoundTag compound)
	{
		return blockTagPredicate.test(compound);
	}

	public boolean MatchesBlockstate(BlockState state)
	{
		return MatchesBlock(state.getBlock()) && statePredicate.test(state);
	}

	public boolean MatchesBlock(Block block)
	{
		return baseBlock == Blocks.AIR && !mustBeAirBlock || baseBlock == block;
	}

	public boolean AllowsAnyBlock()
	{
		return !mustBeAirBlock;
	}
}
