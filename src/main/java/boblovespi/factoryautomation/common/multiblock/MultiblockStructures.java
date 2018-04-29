package boblovespi.factoryautomation.common.multiblock;

import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import static boblovespi.factoryautomation.common.multiblock.MultiblockPart.EMPTY;

/**
 * Created by Willi on 12/24/2017.
 */
public class MultiblockStructures
{
	public static final MultiblockPart NETHER_BRICK = new MultiblockPart(Blocks.NETHER_BRICK);
	public static final MultiblockPart IRON_BLOCK = new MultiblockPart(Blocks.IRON_BLOCK);
	public static final MultiblockPart AIR = new MultiblockPart(Blocks.AIR, true);

	public static final Block[][][] blastFurnace = new Block[][][] {
			new Block[][] {
					new Block[] { Blocks.NETHER_BRICK, FABlocks.blastFurnaceController.ToBlock(), Blocks.NETHER_BRICK },
					new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.AIR },
					new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.AIR },
					new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.AIR },
					new Block[] { Blocks.AIR, Blocks.AIR, Blocks.AIR } },
			new Block[][] {
					new Block[] { Blocks.NETHER_BRICK, Blocks.CAULDRON, Blocks.HOPPER },
					new Block[] { Blocks.IRON_BLOCK, Blocks.NETHER_BRICK, Blocks.IRON_BLOCK },
					new Block[] { Blocks.IRON_BLOCK, Blocks.NETHER_BRICK, Blocks.IRON_BLOCK },
					new Block[] { Blocks.IRON_BLOCK, Blocks.NETHER_BRICK, Blocks.IRON_BLOCK },
					new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.HOPPER } },
			new Block[][] {
				new Block[] { Blocks.NETHER_BRICK, Blocks.NETHER_BRICK, Blocks.NETHER_BRICK },
				new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.AIR },
				new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.AIR },
				new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.AIR },
				new Block[] { Blocks.AIR, Blocks.AIR, Blocks.AIR } } };

	public static final MultiblockPart[][][] steelmakingFurnace = new MultiblockPart[][][]{
			new MultiblockPart[][] {
					new MultiblockPart[] { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY },
					new MultiblockPart[] { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY },
					new MultiblockPart[] { EMPTY, EMPTY, NETHER_BRICK, EMPTY, EMPTY },
					new MultiblockPart[] { EMPTY, EMPTY, NETHER_BRICK, EMPTY, EMPTY }
			},
			new MultiblockPart[][] {
					new MultiblockPart[] { IRON_BLOCK, EMPTY, EMPTY, EMPTY, IRON_BLOCK },
					new MultiblockPart[] { IRON_BLOCK, EMPTY, new MultiblockPart(FABlocks.steelmakingFurnaceController.ToBlock()), EMPTY, IRON_BLOCK },
					new MultiblockPart[] { IRON_BLOCK, NETHER_BRICK, EMPTY, NETHER_BRICK, IRON_BLOCK },
					new MultiblockPart[] { IRON_BLOCK, NETHER_BRICK, EMPTY, NETHER_BRICK, IRON_BLOCK }
			},
			new MultiblockPart[][] {
					new MultiblockPart[] { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY },
					new MultiblockPart[] { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY },
					new MultiblockPart[] { EMPTY, EMPTY, NETHER_BRICK, EMPTY, EMPTY },
					new MultiblockPart[] { EMPTY, EMPTY, NETHER_BRICK, EMPTY, EMPTY }
			},
	};

	public static final MultiblockPart[][][] copperBoiler = new MultiblockPart[][][]{
			new MultiblockPart[][]{
					new MultiblockPart[] {}
			}
	};
}
