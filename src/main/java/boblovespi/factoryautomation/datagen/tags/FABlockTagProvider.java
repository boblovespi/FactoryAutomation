package boblovespi.factoryautomation.datagen.tags;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

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
	}
}
