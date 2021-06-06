package boblovespi.factoryautomation.datagen.loottable;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.Ore;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.SetCount;

public class FABlockLootTables extends BlockLootTables
{
	@Override
	protected void addTables()
	{
		super.addTables();
		// multiblock controllers
		dropSelf(FABlocks.steelmakingFurnaceController.toBlock());
		dropSelf(FABlocks.blastFurnaceController.toBlock());
		dropSelf(FABlocks.tripHammerController.toBlock());
		dropSelf(FABlocks.waterwheel.toBlock());

		// misc
		dropSelf(FABlocks.concrete.toBlock());
//		add(FABlocks.riceCrop.ToBlock(), withExplosionDecay(
//				FABlocks.riceCrop,
//				LootTable.builder().addLootPool(LootPool.lootPool().add(ItemLootEntry.lootTableItem(FAItems.riceGrain)))
//						 .addLootPool(LootPool.lootPool().apply(BlockStateProperty.hasBlockStateProperties(FABlocks.riceCrop.ToBlock()).setProperties(
//							 StatePropertiesPredicate.ANY))
//								  .add(ItemLootEntry.lootTableItem(FAItems.riceGrain))
//								  .apply(SetCount.setCount(RandomValueRange.between(0, 3)))
//								  .apply(ApplyBonus.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))));
		add(FABlocks.concreteSlab, BlockLootTables::createSlabItemTable);
		// handle multiblock part separately
		add(FABlocks.multiblockPart.toBlock(), noDrop());
		dropSelf(FABlocks.cable.toBlock());
		dropSelf(FABlocks.solarPanel.toBlock());
		dropSelf(FABlocks.treetap.toBlock());
		// handle bucket separately
		add(FABlocks.placedBucket.toBlock(), noDrop());
		dropSelf(FABlocks.factorySign.toBlock());
		dropSelf(FABlocks.solidfueledfirebox.toBlock());
		FABlocks.woodChoppingBlocks.forEach(n -> dropSelf(n.toBlock()));
		dropSelf(FABlocks.campfire.toBlock());
		dropSelf(FABlocks.brickMakerFrame.toBlock());
		dropSelf(FABlocks.ironCharcoalMix.toBlock());

		// metal blocks
		for (Metals metal : Metals.values())
		{
			if (metal != Metals.IRON && metal != Metals.GOLD)
				dropSelf(FABlocks.metalBlock.GetBlock(metal));
			dropSelf(FABlocks.metalPlateBlock.GetBlock(metal));
		}
		dropSelf(FABlocks.ironPatternedPlateBlock.toBlock());
		dropSelf(FABlocks.pillarBronze.toBlock());
		dropSelf(FABlocks.pillarCopper.toBlock());
		dropSelf(FABlocks.pillarIron.toBlock());
		dropSelf(FABlocks.pillarMagmaticBrass.toBlock());
		dropSelf(FABlocks.pillarPigIron.toBlock());
		dropSelf(FABlocks.pillarSteel.toBlock());
		dropSelf(FABlocks.pillarTin.toBlock());
		dropSelf(FABlocks.altpillarBronze.toBlock());
		dropSelf(FABlocks.altpillarCopper.toBlock());
		dropSelf(FABlocks.altpillarIron.toBlock());
		dropSelf(FABlocks.altpillarMagmaticBrass.toBlock());
		dropSelf(FABlocks.altpillarPigIron.toBlock());
		dropSelf(FABlocks.altpillarSteel.toBlock());
		dropSelf(FABlocks.altpillarTin.toBlock());

		// ores
		for (MetalOres ore : MetalOres.values())
		{
			dropSelf(FABlocks.metalOres.GetBlock(ore));
		}
		for (Ore.Grade grade : Ore.Grade.values())
		{
			dropSelf(FABlocks.limoniteOre.GetBlock(grade));
			dropSelf(FABlocks.magnetiteOre.GetBlock(grade));
		}
		add(FABlocks.siliconQuartzOre.toBlock(), LootTable.lootTable().withPool(
				LootPool.lootPool().add(ItemLootEntry.lootTableItem(FAItems.siliconQuartz))
						.apply(ApplyBonus.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
		dropSelf(FABlocks.rock.toBlock());
		dropOther(FABlocks.flintRock.toBlock(), Items.FLINT);

		// workbenches
		dropSelf(FABlocks.stoneWorkbench.toBlock());
		dropSelf(FABlocks.ironWorkbench.toBlock());
		dropSelf(FABlocks.chipCreator.toBlock());

		// no fluids

		// decoration blocks
		dropSelf(FABlocks.bronzeCauldron.toBlock());
		dropSelf(FABlocks.bronzeFence.toBlock());
		dropWhenSilkTouch(FABlocks.slagGlass.toBlock());

		// smelting
		dropSelf(FABlocks.stoneCrucible.toBlock());
		dropSelf(FABlocks.stoneCastingVessel.toBlock());
		dropSelf(FABlocks.brickCrucible.toBlock());
		dropSelf(FABlocks.brickCastingVessel.toBlock());
		dropSelf(FABlocks.brickFirebox.toBlock());
		dropSelf(FABlocks.paperBellows.toBlock());
		dropSelf(FABlocks.leatherBellows.toBlock());

		// mechanical
		dropSelf(FABlocks.powerShaft.toBlock());
		dropSelf(FABlocks.gearbox.toBlock());
		dropSelf(FABlocks.bevelGear.toBlock());
		dropSelf(FABlocks.jawCrusher.toBlock());
		dropSelf(FABlocks.creativeMechanicalSource.toBlock());
		dropSelf(FABlocks.motor.toBlock());
		dropSelf(FABlocks.handCrank.toBlock());
		dropSelf(FABlocks.millstone.toBlock());
		dropSelf(FABlocks.horseEngine.toBlock());

		// transfer
		dropSelf(FABlocks.pipe.toBlock());
		dropSelf(FABlocks.pump.toBlock());

		// resource blocks
		dropSelf(FABlocks.greenSand.toBlock());
		add(FABlocks.charcoalPile.toBlock(), LootTable.lootTable().withPool(
				LootPool.lootPool().add(ItemLootEntry.lootTableItem(Items.CHARCOAL))
						.apply(SetCount.setCount(RandomValueRange.between(7, 10)))
						.apply(ApplyBonus.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
		dropSelf(FABlocks.logPile.toBlock());
		dropSelf(FABlocks.terraclayBrickBlock.toBlock());
		dropSelf(FABlocks.terraclayBlock.toBlock());
		add(FABlocks.ironBloom.toBlock(), LootTable.lootTable().withPool(
				LootPool.lootPool().add(ItemLootEntry.lootTableItem(FAItems.ironShard))
						.apply(SetCount.setCount(RandomValueRange.between(2, 4)))
						.apply(ApplyBonus.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))).withPool(
				LootPool.lootPool().add(ItemLootEntry.lootTableItem(FAItems.slag))
						.apply(ApplyBonus.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
	}
}
