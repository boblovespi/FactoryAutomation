package boblovespi.factoryautomation.common.multiblock;

import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;

import java.util.function.Predicate;

/**
 * Created by Willi on 12/24/2017.
 */
public class MultiblockPart
{
	public static final MultiblockPart EMPTY = new MultiblockPart(Blocks.AIR, false);

	private Block baseBlock;
	private Predicate<CompoundNBT> blockTagPredicate;
	private Predicate<BlockState> statePredicate;
	private boolean mustBeAirBlock;

	public MultiblockPart(Block baseBlock, Predicate<CompoundNBT> blockTagPredicate,
			Predicate<BlockState> statePredicate, boolean mustBeAirBlock)
	{
		this.baseBlock = baseBlock;
		this.blockTagPredicate = blockTagPredicate;
		this.statePredicate = statePredicate;
		this.mustBeAirBlock = mustBeAirBlock;
	}

	public MultiblockPart(Block baseBlock, boolean mustBeAirBlock)
	{
		this(baseBlock, Predicates.alwaysTrue(), Predicates.alwaysTrue(), mustBeAirBlock);
	}

	public MultiblockPart(Block baseBlock)
	{
		this(baseBlock, Predicates.alwaysTrue(), Predicates.alwaysTrue(), true);
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

	public boolean MatchesNBT(CompoundNBT compound)
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
