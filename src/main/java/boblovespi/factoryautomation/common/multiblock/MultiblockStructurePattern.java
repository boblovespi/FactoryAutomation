package boblovespi.factoryautomation.common.multiblock;

import net.minecraft.block.Block;

/**
 * Created by Willi on 11/17/2017.
 * the pattern for the multiblocks
 */
public class MultiblockStructurePattern
{
	private Block[][][] pattern;
	private int[] controllerOffset;

	public MultiblockStructurePattern(Block[][][] structurePattern,
			int[] structureControllerOffset)
	{
		pattern = structurePattern;
		controllerOffset = structureControllerOffset;
	}

	public Block[][][] GetPattern()
	{
		return pattern;
	}

	public int[] getControllerOffset()
	{
		return controllerOffset;
	}
}
