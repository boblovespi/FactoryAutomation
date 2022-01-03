package boblovespi.factoryautomation.datagen.tags;

import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.Ore;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;
import java.util.stream.Collectors;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class FABlockTagProvider extends BlockTagsProvider
{
	public FABlockTagProvider(DataGenerator dataGenerator, ExistingFileHelper existingHelper)
	{
		super(dataGenerator, MODID, existingHelper);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags()
	{
		tag(FATags.CreateForgeBlockTag("concrete")).add(FABlocks.concrete.ToBlock());
		tag(FATags.CreateForgeBlockTag("clay")).add(Blocks.CLAY);
		tag(FATags.CreateForgeBlockTag("slabs/cobblestone"))
				.add(Blocks.COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB);
		tag(FATags.CreateForgeBlockTag("dirt")).add(Blocks.DIRT, Blocks.COARSE_DIRT);
		for (int i = 2; i < Metals.values().length; ++i)
		{
			tag(FATags.CreateForgeBlockTag("storage_blocks/" + Metals.values()[i].name()))
					.add(FABlocks.metalBlock.GetBlock(Metals.values()[i]));
		}
		for (MetalOres ore : MetalOres.values())
		{
			tag(FATags.CreateForgeBlockTag("ores/" + ore.name())).add(FABlocks.metalOres.GetBlock(ore));
		}
		tag(FATags.CreateFABlockTag("storage_blocks/t5")).addTags(FATags.CreateForgeBlockTag("storage_blocks/steel"));
		tag(FATags.CreateFABlockTag("storage_blocks/t4"))
				.addTags(FATags.CreateForgeBlockTag("storage_blocks/magmatic_brass"), FATags.CreateFABlockTag("storage_blocks/t5"));
		tag(FATags.CreateFABlockTag("storage_blocks/t3"))
				.addTags(FATags.CreateForgeBlockTag("storage_blocks/bronze"), FATags.CreateFABlockTag("storage_blocks/t4"));
		tag(FATags.CreateFABlockTag("storage_blocks/t2"))
				.addTags(FATags.CreateForgeBlockTag("storage_blocks/iron"), FATags.CreateFABlockTag("storage_blocks/t3"));
		tag(FATags.CreateFABlockTag("storage_blocks/t1"))
				.addTags(FATags.CreateForgeBlockTag("storage_blocks/copper"), FATags.CreateFABlockTag("storage_blocks/t2"));

		tag(FATags.CreateFABlockTag("campfire")).add(FABlocks.campfire.ToBlock(), Blocks.CAMPFIRE);

		tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.add(Arrays.stream(Metals.values()).skip(3).map(n -> FABlocks.metalBlock.GetBlock(n).ToBlock())
							 .toArray(Block[]::new))
				.add(Arrays.stream(Metals.values()).map(n -> FABlocks.metalPlateBlock.GetBlock(n).ToBlock())
							 .toArray(Block[]::new))
				.add(Arrays.stream(Ore.Grade.values()).map(n -> FABlocks.limoniteOre.GetBlock(n).ToBlock())
							 .toArray(Block[]::new))
				.add(Arrays.stream(MetalOres.values()).map(n -> FABlocks.metalOres.GetBlock(n).ToBlock())
							 .toArray(Block[]::new))
				.add(FABlocks.ironPatternedPlateBlock.ToBlock(), FABlocks.factorySign.ToBlock(), FABlocks.terraclayBrickBlock.ToBlock(), FABlocks.slagGlass.ToBlock())
				.add(FABlocks.cassiteriteRawBlock.ToBlock(), FABlocks.limoniteRawBlock.ToBlock(), FABlocks.brickFirebox.ToBlock(), FABlocks.brickCrucible.ToBlock());
		tag(BlockTags.MINEABLE_WITH_AXE).add(FABlocks.brickMakerFrame.ToBlock(), FABlocks.logPile.ToBlock()).add(FABlocks.woodChoppingBlocks.stream().map(FABlock::ToBlock).toArray(Block[]::new))
				.add(FABlocks.campfire.ToBlock());
		tag(BlockTags.MINEABLE_WITH_SHOVEL).add(FABlocks.charcoalPile.ToBlock()).add(FABlocks.ironCharcoalMix.ToBlock()).add(FABlocks.greenSand.ToBlock()).add(FABlocks.terraclayBlock.ToBlock());
		tag(FATags.HAMMER_TOOL).add(Blocks.STONE).add(FABlocks.ironBloom.ToBlock()).add(Blocks.COBBLESTONE);
		tag(FATags.NEEDS_FLINT_TOOL);
		tag(BlockTags.NEEDS_STONE_TOOL).add(FABlocks.cassiteriteRawBlock.ToBlock(), FABlocks.limoniteRawBlock.ToBlock());
		tag(FATags.NEEDS_COPPER_TOOL)
				.add(Arrays.stream(Ore.Grade.values()).skip(3).map(n -> FABlocks.limoniteOre.GetBlock(n).ToBlock())
							 .toArray(Block[]::new))
				.add(Arrays.stream(Metals.values()).skip(3).map(n -> FABlocks.metalBlock.GetBlock(n).ToBlock())
							 .toArray(Block[]::new))
				.add(Arrays.stream(Metals.values()).map(n -> FABlocks.metalPlateBlock.GetBlock(n).ToBlock())
							 .toArray(Block[]::new)).add(FABlocks.ironPatternedPlateBlock.ToBlock()).add(FABlocks.factorySign.ToBlock());
	}
}
