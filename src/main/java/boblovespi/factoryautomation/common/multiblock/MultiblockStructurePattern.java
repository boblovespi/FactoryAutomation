package boblovespi.factoryautomation.common.multiblock;

import boblovespi.factoryautomation.common.util.NotYetImplemented;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;

/**
 * Created by Willi on 11/17/2017.
 * the pattern for the multiblocks
 */
public class MultiblockStructurePattern
{
	private MultiblockPart[][][] pattern;
	private int[] controllerOffset;

	public MultiblockStructurePattern(MultiblockPart[][][] structurePattern,
			int[] structureControllerOffset)
	{
		pattern = structurePattern;
		controllerOffset = structureControllerOffset;
	}

	public MultiblockStructurePattern(Block[][][] structurePattern,
			int[] structureControllerOffset)
	{
		pattern = MultiblockPart.FromBlocks(structurePattern);
		controllerOffset = structureControllerOffset;
	}

	public MultiblockPart[][][] GetPattern()
	{
		return pattern;
	}

	public int[] GetControllerOffset()
	{
		return controllerOffset;
	}

	@NotYetImplemented
	public static MultiblockStructurePattern FromNBT(CompoundNBT compound)
	{
		return null;
	}
}
