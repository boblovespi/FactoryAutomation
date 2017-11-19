package boblovespi.factoryautomation.common.multiblock;

import net.minecraft.block.Block;

/**
 * Created by Willi on 11/17/2017.
 * the pattern for the multiblocks
 */
public class MultiblockStructurePattern
{
	private Block[][][] pattern;

	public MultiblockStructurePattern(Block[][][] structurePattern)
	{
		pattern = structurePattern;
	}

	public Block[][][] GetPattern()
	{
		return pattern;
	}
}
