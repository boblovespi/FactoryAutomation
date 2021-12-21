package boblovespi.factoryautomation.common.multiblock;

import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;

import static boblovespi.factoryautomation.common.multiblock.MultiblockPart.EMPTY;

/**
 * Created by Willi on 12/24/2017.
 */
public class MultiblockStructures
{
	public static final MultiblockPart NETHER_BRICK = new MultiblockPart(Blocks.NETHER_BRICKS);
	public static final MultiblockPart IRON_BLOCK = new MultiblockPart(Blocks.IRON_BLOCK);
	public static final MultiblockPart AIR = new MultiblockPart(Blocks.AIR, true);
	public static final MultiblockPart COBBLESTONE_WALL = new MultiblockPart(Blocks.COBBLESTONE_WALL);
	public static final MultiblockPart OAK_FENCE = new MultiblockPart(Blocks.OAK_FENCE);
	public static final MultiblockPart OAK_STAIR = new MultiblockPart(Blocks.OAK_STAIRS);
	public static final MultiblockPart OAK_PLANK = new MultiblockPart(Blocks.OAK_PLANKS);
	public static final MultiblockPart OAK_LOG_UP = new MultiblockPart(Blocks.OAK_LOG, n -> true,
			n -> n.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y, false);
	public static final MultiblockPart BRICK = new MultiblockPart(Blocks.BRICKS);

	public static final Block[][][] blastFurnace = new Block[][][] {
			new Block[][] {
					new Block[] { Blocks.NETHER_BRICKS, FABlocks.blastFurnaceController.ToBlock(), Blocks.NETHER_BRICKS },
					new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.AIR },
					new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.AIR },
					new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.AIR },
					new Block[] { Blocks.AIR, Blocks.AIR, Blocks.AIR } },
			new Block[][] {
					new Block[] { Blocks.NETHER_BRICKS, Blocks.CAULDRON, Blocks.HOPPER },
					new Block[] { Blocks.IRON_BLOCK, Blocks.NETHER_BRICKS, Blocks.IRON_BLOCK },
					new Block[] { Blocks.IRON_BLOCK, Blocks.NETHER_BRICKS, Blocks.IRON_BLOCK },
					new Block[] { Blocks.IRON_BLOCK, Blocks.NETHER_BRICKS, Blocks.IRON_BLOCK },
					new Block[] { Blocks.AIR, Blocks.IRON_BLOCK, Blocks.HOPPER } },
			new Block[][] {
				new Block[] { Blocks.NETHER_BRICKS, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICKS },
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

	public static final MultiblockPart[][][] copperBoiler = new MultiblockPart[][][] {
			new MultiblockPart[][]{
					new MultiblockPart[] {}
			}
	};

	public static final MultiblockPart[][][] tripHammer = new MultiblockPart[][][] {
			new MultiblockPart[][]{
					new MultiblockPart[] { new MultiblockPart(FABlocks.tripHammerController.ToBlock()) },
					new MultiblockPart[] { IRON_BLOCK }
			},
			new MultiblockPart[][]{
					new MultiblockPart[] { AIR },
					new MultiblockPart[] { OAK_FENCE }
			},
			new MultiblockPart[][]{
					new MultiblockPart[] { AIR },
					new MultiblockPart[] { OAK_FENCE }
			},
			new MultiblockPart[][]{
					new MultiblockPart[] { OAK_LOG_UP },
					new MultiblockPart[] { OAK_LOG_UP }
			},
			new MultiblockPart[][]{
					new MultiblockPart[] { AIR },
					new MultiblockPart[] { OAK_FENCE }
			},
			new MultiblockPart[][]{
					new MultiblockPart[] { AIR },
					new MultiblockPart[] { IRON_BLOCK }
			}
	};

	public static final MultiblockPart[][][] stoneFoundry = new MultiblockPart[][][] {
			new MultiblockPart[][]{
					new MultiblockPart[] { new MultiblockPart(Blocks.FURNACE) },
					new MultiblockPart[] { new MultiblockPart(FABlocks.stoneCrucible.ToBlock()) }
			}
	};

	public static final MultiblockPart[][][] brickFoundry = new MultiblockPart[][][] {
			new MultiblockPart[][]{
					new MultiblockPart[] { new MultiblockPart(FABlocks.brickFirebox.ToBlock()) },
					new MultiblockPart[] { new MultiblockPart(FABlocks.brickCrucible.ToBlock()) }
			}
	};

//	public static final MultiblockPart[][][] brickKiln = new MultiblockPart[][][] {
//			new MultiblockPart[][] {
//					new MultiblockPart[] { BRICK, BRICK, BRICK },
//					new MultiblockPart[] { BRICK, BRICK, BRICK },
//					new MultiblockPart[] { BRICK, BRICK, BRICK }
//			},
//			new MultiblockPart[][] {
//					new MultiblockPart[] { BRICK, new MultiblockPart(FABlocks.brickKiln.ToBlock()), BRICK },
//					new MultiblockPart[] { BRICK, BRICK, BRICK },
//					new MultiblockPart[] { BRICK, BRICK, BRICK }
//			},
//			new MultiblockPart[][] {
//					new MultiblockPart[] { BRICK, BRICK, BRICK },
//					new MultiblockPart[] { BRICK, BRICK, BRICK },
//					new MultiblockPart[] { BRICK, BRICK, BRICK }
//			}
//	};

	public static final MultiblockPart[][][] waterwheel = new MultiblockPart[][][] {
			new MultiblockPart[][] {
					new MultiblockPart[] { EMPTY    , OAK_STAIR, OAK_PLANK, OAK_STAIR, EMPTY },
					new MultiblockPart[] { OAK_STAIR, AIR      , OAK_FENCE, AIR      , OAK_STAIR },
					new MultiblockPart[] { OAK_PLANK, OAK_FENCE, new MultiblockPart(FABlocks.waterwheel.ToBlock()), OAK_FENCE, OAK_PLANK },
					new MultiblockPart[] { OAK_STAIR, AIR      , OAK_FENCE, AIR      , OAK_STAIR },
					new MultiblockPart[] { EMPTY    , OAK_STAIR, OAK_PLANK, OAK_STAIR, EMPTY }
			}
	};

}
