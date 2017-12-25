package boblovespi.factoryautomation.common.multiblock;

import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Predicate;

/**
 * Created by Willi on 12/24/2017.
 */
public class MultiblockPart
{
	public static final MultiblockPart EMPTY = new MultiblockPart(
			Blocks.AIR, false);

	private Block baseBlock;
	private Predicate<NBTTagCompound> blockTagPredicate;
	private Predicate<IBlockState> statePredicate;
	private boolean mustBeAirBlock;

	public MultiblockPart(Block baseBlock,
			Predicate<NBTTagCompound> blockTagPredicate,
			Predicate<IBlockState> statePredicate, boolean mustBeAirBlock)
	{
		this.baseBlock = baseBlock;
		this.blockTagPredicate = blockTagPredicate;
		this.statePredicate = statePredicate;
		this.mustBeAirBlock = mustBeAirBlock;
	}

	public MultiblockPart(Block baseBlock, boolean mustBeAirBlock)
	{
		this(baseBlock, Predicates.alwaysTrue(), Predicates.alwaysTrue(),
			 mustBeAirBlock);
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

	public boolean MatchesNBT(NBTTagCompound compound)
	{
		return blockTagPredicate.test(compound);
	}

	public boolean MatchesBlockstate(IBlockState state)
	{
		return statePredicate.test(state);
	}

	public boolean MatchesBlock(Block block)
	{
		return Block.isEqualTo(baseBlock, Blocks.AIR) && !mustBeAirBlock
				|| Block.isEqualTo(baseBlock, block);
	}
}
