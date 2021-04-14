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
		getOrCreateBuilder(FATags.ForgeBlockNamedTag("concrete")).add(FABlocks.concrete.ToBlock());
		getOrCreateBuilder(FATags.ForgeBlockNamedTag("clay")).add(Blocks.CLAY);
		getOrCreateBuilder(FATags.ForgeBlockNamedTag("slabs/cobblestone"))
				.add(Blocks.COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB);
		getOrCreateBuilder(FATags.ForgeBlockNamedTag("dirt")).add(Blocks.DIRT, Blocks.COARSE_DIRT);
		for (int i = 2; i < Metals.values().length; ++i)
		{
			getOrCreateBuilder(FATags.ForgeBlockNamedTag("storage_blocks/" + Metals.values()[i].getString()))
					.add(FABlocks.metalBlock.GetBlock(Metals.values()[i]));
		}
		for (MetalOres ore : MetalOres.values())
		{
			getOrCreateBuilder(FATags.ForgeBlockNamedTag("ores/" + ore.getString())).add(FABlocks.metalOres.GetBlock(ore));
		}
		getOrCreateBuilder(FATags.FABlockNamedTag("storage_blocks/t5")).add(FATags.ForgeBlockNamedTag("storage_blocks/steel"));
		getOrCreateBuilder(FATags.FABlockNamedTag("storage_blocks/t4"))
				.add(FATags.ForgeBlockNamedTag("storage_blocks/magmatic_brass"), FATags.FABlockNamedTag("storage_blocks/t5"));
		getOrCreateBuilder(FATags.FABlockNamedTag("storage_blocks/t3"))
				.add(FATags.ForgeBlockNamedTag("storage_blocks/bronze"), FATags.FABlockNamedTag("storage_blocks/t4"));
		getOrCreateBuilder(FATags.FABlockNamedTag("storage_blocks/t2"))
				.add(FATags.ForgeBlockNamedTag("storage_blocks/iron"), FATags.FABlockNamedTag("storage_blocks/t3"));
		getOrCreateBuilder(FATags.FABlockNamedTag("storage_blocks/t1"))
				.add(FATags.ForgeBlockNamedTag("storage_blocks/copper"), FATags.FABlockNamedTag("storage_blocks/t2"));

		getOrCreateBuilder(FATags.FABlockNamedTag("campfire")).add(FABlocks.campfire.ToBlock(), Blocks.CAMPFIRE);
	}
}
