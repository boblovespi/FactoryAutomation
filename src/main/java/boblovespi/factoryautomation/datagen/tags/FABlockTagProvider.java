package boblovespi.factoryautomation.datagen.tags;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;

public class FABlockTagProvider extends BlockTagsProvider
{
	public FABlockTagProvider(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	@Override
	protected void registerTags()
	{
		getBuilder(FATags.ForgeBlockTag("concrete")).add(FABlocks.concrete.toBlock());
		getBuilder(FATags.ForgeBlockTag("clay")).add(Blocks.CLAY);
		getBuilder(FATags.ForgeBlockTag("slabs/cobblestone"))
				.add(Blocks.COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB);
		getBuilder(FATags.ForgeBlockTag("dirt")).add(Blocks.DIRT, Blocks.COARSE_DIRT);
		for (int i = 2; i < Metals.values().length; ++i)
		{
			getBuilder(FATags.ForgeBlockTag("storage_blocks/" + Metals.values()[i].getName()))
					.add(FABlocks.metalBlock.GetBlock(Metals.values()[i]));
		}
		for (MetalOres ore : MetalOres.values())
		{
			getBuilder(FATags.ForgeBlockTag("ores/" + ore.getName())).add(FABlocks.metalOres.GetBlock(ore));
		}
		getBuilder(FATags.FABlockTag("storage_blocks/t5")).add(FATags.ForgeBlockTag("storage_blocks/steel"));
		getBuilder(FATags.FABlockTag("storage_blocks/t4"))
				.add(FATags.ForgeBlockTag("storage_blocks/magmatic_brass"), FATags.FABlockTag("storage_blocks/t5"));
		getBuilder(FATags.FABlockTag("storage_blocks/t3"))
				.add(FATags.ForgeBlockTag("storage_blocks/bronze"), FATags.FABlockTag("storage_blocks/t4"));
		getBuilder(FATags.FABlockTag("storage_blocks/t2"))
				.add(FATags.ForgeBlockTag("storage_blocks/iron"), FATags.FABlockTag("storage_blocks/t3"));
		getBuilder(FATags.FABlockTag("storage_blocks/t1"))
				.add(FATags.ForgeBlockTag("storage_blocks/copper"), FATags.FABlockTag("storage_blocks/t2"));

		getBuilder(FATags.FABlockTag("campfire")).add(FABlocks.campfire.toBlock(), Blocks.CAMPFIRE);
	}
}
