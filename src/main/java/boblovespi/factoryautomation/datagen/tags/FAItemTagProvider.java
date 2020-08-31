package boblovespi.factoryautomation.datagen.tags;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;

public class FAItemTagProvider extends ItemTagsProvider
{
	public FAItemTagProvider(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	@Override
	protected void registerTags()
	{
		copy(FATags.ForgeBlockTag("concrete"), FATags.ForgeItemTag("concrete"));
		copy(FATags.ForgeBlockTag("clay"), FATags.ForgeItemTag("clay"));
		copy(FATags.ForgeBlockTag("slabs/cobblestone"), FATags.ForgeItemTag("slabs/cobblestone"));
		copy(FATags.ForgeBlockTag("dirt"), FATags.ForgeItemTag("dirt"));

		getBuilder(FATags.ForgeItemTag("slag")).add(FAItems.slag.ToItem());
		getBuilder(FATags.ForgeItemTag("rice")).add(FAItems.riceGrain.ToItem());
		getBuilder(FATags.ForgeItemTag("wires/copper")).add(FAItems.copperWire.ToItem());
		getBuilder(FATags.ForgeItemTag("dusts/stone")).add(FAItems.stoneDust.ToItem());
		getBuilder(FATags.ForgeItemTag("dusts/ash")).add(FAItems.ash.ToItem());
		getBuilder(FATags.ForgeItemTag("dusts/acid")).add(FAItems.acidPowder.ToItem());
		getBuilder(FATags.ForgeItemTag("bowls/glycerin")).add(FAItems.liquidGlycerin.ToItem());
		getBuilder(FATags.ForgeItemTag("glycerin")).add(FAItems.dryGlycerin.ToItem());
		getBuilder(FATags.ForgeItemTag("ingots/rubber")).add(FAItems.rubber.ToItem());
		getBuilder(FATags.ForgeItemTag("gems/graphite")).add(FAItems.graphite.ToItem());
		getBuilder(FATags.ForgeItemTag("tallow")).add(FAItems.pigTallow.ToItem());

		for (MetalOres ore : MetalOres.values())
		{
			copy(FATags.ForgeBlockTag("ores/" + ore.getName()), FATags.ForgeItemTag("ores/" + ore.getName()));
		}

		for (int i = 2; i < Metals.values().length; ++i)
		{
			copy(FATags.ForgeBlockTag("storage_blocks/" + Metals.values()[i].getName()),
					FATags.ForgeItemTag("storage_blocks/" + Metals.values()[i].getName()));
			getBuilder(FATags.ForgeItemTag("ingots/" + Metals.values()[i].getName()))
					.add(FAItems.ingot.GetItem(Metals.values()[i]));
			getBuilder(FATags.ForgeItemTag("nuggets/" + Metals.values()[i].getName()))
					.add(FAItems.nugget.GetItem(Metals.values()[i]));
		}

		for (int i = 0; i < Metals.values().length; ++i)
		{
			getBuilder(FATags.ForgeItemTag("plates/" + Metals.values()[i].getName()))
					.add(FAItems.sheet.GetItem(Metals.values()[i]));
			getBuilder(FATags.ForgeItemTag("rods/" + Metals.values()[i].getName()))
					.add(FAItems.rod.GetItem(Metals.values()[i]));
		}

		getBuilder(FATags.FAItemTag("ingots/t5")).add(FATags.ForgeItemTag("ingots/steel"));
		getBuilder(FATags.FAItemTag("ingots/t4"))
				.add(FATags.ForgeItemTag("ingots/magmatic_brass"), FATags.FAItemTag("ingots/t5"));
		getBuilder(FATags.FAItemTag("ingots/t3"))
				.add(FATags.ForgeItemTag("ingots/bronze"), FATags.FAItemTag("ingots/t4"));
		getBuilder(FATags.FAItemTag("ingots/t2"))
				.add(FATags.ForgeItemTag("ingots/iron"), FATags.FAItemTag("ingots/t3"));
		getBuilder(FATags.FAItemTag("ingots/t1"))
				.add(FATags.ForgeItemTag("ingots/copper"), FATags.FAItemTag("ingots/t2"));

		getBuilder(FATags.FAItemTag("nuggets/t5")).add(FATags.ForgeItemTag("nuggets/steel"));
		getBuilder(FATags.FAItemTag("nuggets/t4"))
				.add(FATags.ForgeItemTag("nuggets/magmatic_brass"), FATags.FAItemTag("nuggets/t5"));
		getBuilder(FATags.FAItemTag("nuggets/t3"))
				.add(FATags.ForgeItemTag("nuggets/bronze"), FATags.FAItemTag("nuggets/t4"));
		getBuilder(FATags.FAItemTag("nuggets/t2"))
				.add(FATags.ForgeItemTag("nuggets/iron"), FATags.FAItemTag("nuggets/t3"));
		getBuilder(FATags.FAItemTag("nuggets/t1"))
				.add(FATags.ForgeItemTag("nuggets/copper"), FATags.FAItemTag("nuggets/t2"));

		getBuilder(FATags.FAItemTag("plates/t5")).add(FATags.ForgeItemTag("plates/steel"));
		getBuilder(FATags.FAItemTag("plates/t4"))
				.add(FATags.ForgeItemTag("plates/magmatic_brass"), FATags.FAItemTag("plates/t5"));
		getBuilder(FATags.FAItemTag("plates/t3"))
				.add(FATags.ForgeItemTag("plates/bronze"), FATags.FAItemTag("plates/t4"));
		getBuilder(FATags.FAItemTag("plates/t2"))
				.add(FATags.ForgeItemTag("plates/iron"), FATags.FAItemTag("plates/t3"));
		getBuilder(FATags.FAItemTag("plates/t1"))
				.add(FATags.ForgeItemTag("plates/copper"), FATags.FAItemTag("plates/t2"));

		getBuilder(FATags.FAItemTag("rods/t5")).add(FATags.ForgeItemTag("rods/steel"));
		getBuilder(FATags.FAItemTag("rods/t4"))
				.add(FATags.ForgeItemTag("rods/magmatic_brass"), FATags.FAItemTag("rods/t5"));
		getBuilder(FATags.FAItemTag("rods/t3")).add(FATags.ForgeItemTag("rods/bronze"), FATags.FAItemTag("rods/t4"));
		getBuilder(FATags.FAItemTag("rods/t2")).add(FATags.ForgeItemTag("rods/iron"), FATags.FAItemTag("rods/t3"));
		getBuilder(FATags.FAItemTag("rods/t1")).add(FATags.ForgeItemTag("rods/copper"), FATags.FAItemTag("rods/t2"));

		copy(FATags.FABlockTag("storage_blocks/t5"), FATags.FAItemTag("storage_blocks/t5"));
		copy(FATags.FABlockTag("storage_blocks/t4"), FATags.FAItemTag("storage_blocks/t4"));
		copy(FATags.FABlockTag("storage_blocks/t3"), FATags.FAItemTag("storage_blocks/t3"));
		// copy(FATags.FABlockTag("storage_blocks/t2"), FATags.FAItemTag("storage_blocks/t2"));
		// copy(FATags.FABlockTag("storage_blocks/t1"), FATags.FAItemTag("storage_blocks/t1"));

		getBuilder(FATags.FAItemTag("tools/axes"))
				.add(Items.WOODEN_AXE, Items.STONE_AXE, Items.GOLDEN_AXE, Items.IRON_AXE, Items.DIAMOND_AXE,
						FAItems.bronzeAxe.ToItem(), FAItems.steelAxe.ToItem(), FAItems.copperAxe.ToItem(),
						FAItems.choppingBlade.ToItem());
		getBuilder(FATags.FAItemTag("tools/silks_grass")).add(Items.SHEARS, FAItems.choppingBlade.ToItem());
	}
}
