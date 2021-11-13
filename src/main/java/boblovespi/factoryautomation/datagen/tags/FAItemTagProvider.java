package boblovespi.factoryautomation.datagen.tags;

import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class FAItemTagProvider extends ItemTagsProvider
{
	public FAItemTagProvider(DataGenerator dataGenerator, FABlockTagProvider blockTags, ExistingFileHelper helper)
	{
		super(dataGenerator, blockTags, MODID, helper);
	}

	@Override
	protected void addTags()
	{
		copy(FATags.CreateForgeBlockTag("concrete"), FATags.CreateForgeItemTag("concrete"));
		copy(FATags.CreateForgeBlockTag("clay"), FATags.CreateForgeItemTag("clay"));
		copy(FATags.CreateForgeBlockTag("slabs/cobblestone"), FATags.CreateForgeItemTag("slabs/cobblestone"));
		copy(FATags.CreateForgeBlockTag("dirt"), FATags.CreateForgeItemTag("dirt"));

		tag(FATags.CreateForgeItemTag("slag")).add(FAItems.slag.ToItem());
		tag(FATags.CreateForgeItemTag("rice")).add(FAItems.riceGrain.ToItem());
		tag(FATags.CreateForgeItemTag("wires/copper")).add(FAItems.copperWire.ToItem());
		tag(FATags.CreateForgeItemTag("dusts/stone")).add(FAItems.stoneDust.ToItem());
		tag(FATags.CreateForgeItemTag("dusts/ash")).add(FAItems.ash.ToItem());
		tag(FATags.CreateForgeItemTag("dusts/acid")).add(FAItems.acidPowder.ToItem());
		tag(FATags.CreateForgeItemTag("bowls/glycerin")).add(FAItems.liquidGlycerin.ToItem());
		tag(FATags.CreateForgeItemTag("glycerin")).add(FAItems.dryGlycerin.ToItem());
		tag(FATags.CreateForgeItemTag("ingots/rubber")).add(FAItems.rubber.ToItem());
		tag(FATags.CreateForgeItemTag("gems/graphite")).add(FAItems.graphite.ToItem());
		tag(FATags.CreateForgeItemTag("tallow")).add(FAItems.pigTallow.ToItem());

		for (MetalOres ore : MetalOres.values())
		{
			copy(FATags.CreateForgeBlockTag("ores/" + ore.name()), FATags.CreateForgeItemTag("ores/" + ore.name()));
		}

		for (int i = 2; i < Metals.values().length; ++i)
		{
			copy(FATags.CreateForgeBlockTag("storage_blocks/" + Metals.values()[i].name()),
					FATags.CreateForgeItemTag("storage_blocks/" + Metals.values()[i].name()));
			tag(FATags.CreateForgeItemTag("ingots/" + Metals.values()[i].name()))
					.add(FAItems.ingot.GetItem(Metals.values()[i]));
			tag(FATags.CreateForgeItemTag("nuggets/" + Metals.values()[i].name()))
					.add(FAItems.nugget.GetItem(Metals.values()[i]));
		}

		for (int i = 0; i < Metals.values().length; ++i)
		{
			tag(FATags.CreateForgeItemTag("plates/" + Metals.values()[i].name()))
					.add(FAItems.sheet.GetItem(Metals.values()[i]));
			tag(FATags.CreateForgeItemTag("rods/" + Metals.values()[i].name()))
					.add(FAItems.rod.GetItem(Metals.values()[i]));
		}

		tag(FATags.CreateFAItemTag("ingots/t5")).addTags(FATags.CreateForgeItemTag("ingots/steel"));
		tag(FATags.CreateFAItemTag("ingots/t4"))
				.addTags(FATags.CreateForgeItemTag("ingots/magmatic_brass"), FATags.CreateFAItemTag("ingots/t5"));
		tag(FATags.CreateFAItemTag("ingots/t3"))
				.addTags(FATags.CreateForgeItemTag("ingots/bronze"), FATags.CreateFAItemTag("ingots/t4"));
		tag(FATags.CreateFAItemTag("ingots/t2"))
				.addTags(FATags.CreateForgeItemTag("ingots/iron"), FATags.CreateFAItemTag("ingots/t3"));
		tag(FATags.CreateFAItemTag("ingots/t1"))
				.addTags(FATags.CreateForgeItemTag("ingots/copper"), FATags.CreateFAItemTag("ingots/t2"));

		tag(FATags.CreateFAItemTag("nuggets/t5")).addTags(FATags.CreateForgeItemTag("nuggets/steel"));
		tag(FATags.CreateFAItemTag("nuggets/t4"))
				.addTags(FATags.CreateForgeItemTag("nuggets/magmatic_brass"), FATags.CreateFAItemTag("nuggets/t5"));
		tag(FATags.CreateFAItemTag("nuggets/t3"))
				.addTags(FATags.CreateForgeItemTag("nuggets/bronze"), FATags.CreateFAItemTag("nuggets/t4"));
		tag(FATags.CreateFAItemTag("nuggets/t2"))
				.addTags(FATags.CreateForgeItemTag("nuggets/iron"), FATags.CreateFAItemTag("nuggets/t3"));
		tag(FATags.CreateFAItemTag("nuggets/t1"))
				.addTags(FATags.CreateForgeItemTag("nuggets/copper"), FATags.CreateFAItemTag("nuggets/t2"));

		tag(FATags.CreateFAItemTag("plates/t5")).addTags(FATags.CreateForgeItemTag("plates/steel"));
		tag(FATags.CreateFAItemTag("plates/t4"))
				.addTags(FATags.CreateForgeItemTag("plates/magmatic_brass"), FATags.CreateFAItemTag("plates/t5"));
		tag(FATags.CreateFAItemTag("plates/t3"))
				.addTags(FATags.CreateForgeItemTag("plates/bronze"), FATags.CreateFAItemTag("plates/t4"));
		tag(FATags.CreateFAItemTag("plates/t2"))
				.addTags(FATags.CreateForgeItemTag("plates/iron"), FATags.CreateFAItemTag("plates/t3"));
		tag(FATags.CreateFAItemTag("plates/t1"))
				.addTags(FATags.CreateForgeItemTag("plates/copper"), FATags.CreateFAItemTag("plates/t2"));

		tag(FATags.CreateFAItemTag("rods/t5")).addTags(FATags.CreateForgeItemTag("rods/steel"));
		tag(FATags.CreateFAItemTag("rods/t4"))
				.addTags(FATags.CreateForgeItemTag("rods/magmatic_brass"), FATags.CreateFAItemTag("rods/t5"));
		tag(FATags.CreateFAItemTag("rods/t3")).addTags(FATags.CreateForgeItemTag("rods/bronze"), FATags.CreateFAItemTag("rods/t4"));
		tag(FATags.CreateFAItemTag("rods/t2")).addTags(FATags.CreateForgeItemTag("rods/iron"), FATags.CreateFAItemTag("rods/t3"));
		tag(FATags.CreateFAItemTag("rods/t1")).addTags(FATags.CreateForgeItemTag("rods/copper"), FATags.CreateFAItemTag("rods/t2"));

		copy(FATags.CreateFABlockTag("storage_blocks/t5"), FATags.CreateFAItemTag("storage_blocks/t5"));
		copy(FATags.CreateFABlockTag("storage_blocks/t4"), FATags.CreateFAItemTag("storage_blocks/t4"));
		copy(FATags.CreateFABlockTag("storage_blocks/t3"), FATags.CreateFAItemTag("storage_blocks/t3"));
		// copy(FATags.CreateFABlockTag("storage_blocks/t2"), FATags.CreateFAItemTag("storage_blocks/t2"));
		// copy(FATags.CreateFABlockTag("storage_blocks/t1"), FATags.CreateFAItemTag("storage_blocks/t1"));
		copy(FATags.CreateFABlockTag("campfire"), FATags.CreateFAItemTag("campfire"));

		tag(FATags.CreateFAItemTag("tools/axes"))
				.add(Items.WOODEN_AXE, Items.STONE_AXE, Items.GOLDEN_AXE, Items.IRON_AXE, Items.DIAMOND_AXE,
						FAItems.bronzeAxe.ToItem(), FAItems.steelAxe.ToItem(), FAItems.copperAxe.ToItem(),
						FAItems.choppingBlade.ToItem());
		tag(FATags.CreateFAItemTag("tools/silks_grass")).add(Items.SHEARS, FAItems.choppingBlade.ToItem());
		tag(FATags.CreateFAItemTag("tools/good_axes"))
				.add(Items.IRON_AXE, Items.DIAMOND_AXE, FAItems.bronzeAxe.ToItem(), FAItems.steelAxe.ToItem());
	}
}
