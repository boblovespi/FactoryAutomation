package boblovespi.factoryautomation.datagen.loottable;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.RiceCrop;
import boblovespi.factoryautomation.common.block.resource.Ore;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.SetCount;

public class FABlockLootTables extends BlockLootTables
{
	@Override
	protected void addTables()
	{
		super.addTables();
		// multiblock controllers
		registerDropSelfLootTable(FABlocks.steelmakingFurnaceController.ToBlock());
		registerDropSelfLootTable(FABlocks.blastFurnaceController.ToBlock());
		registerDropSelfLootTable(FABlocks.tripHammerController.ToBlock());
		registerDropSelfLootTable(FABlocks.waterwheel.ToBlock());

		// misc
		registerDropSelfLootTable(FABlocks.concrete.ToBlock());
		registerLootTable(FABlocks.riceCrop.ToBlock(), withExplosionDecay(
				FABlocks.riceCrop,
				LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(FAItems.riceGrain)))
						 .addLootPool(LootPool.builder().acceptCondition(
								 BlockStateProperty.builder(FABlocks.riceCrop.ToBlock()).fromProperties(
										 StatePropertiesPredicate.Builder.newBuilder().withIntProp(RiceCrop.AGE, 7)))
											  .addEntry(ItemLootEntry.builder(FAItems.riceGrain))
											  .acceptFunction(SetCount.builder(RandomValueRange.of(0, 3)))
											  .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE)))));
		registerLootTable(FABlocks.concreteSlab, BlockLootTables::droppingSlab);
		// handle multiblock part separately
		registerLootTable(FABlocks.multiblockPart.ToBlock(), blockNoDrop());
		registerDropSelfLootTable(FABlocks.cable.ToBlock());
		registerDropSelfLootTable(FABlocks.solarPanel.ToBlock());
		registerDropSelfLootTable(FABlocks.treetap.ToBlock());
		// handle bucket separately
		registerLootTable(FABlocks.placedBucket.ToBlock(), blockNoDrop());
		registerDropSelfLootTable(FABlocks.factorySign.ToBlock());
		registerDropSelfLootTable(FABlocks.solidfueledfirebox.ToBlock());
		FABlocks.woodChoppingBlocks.forEach(n -> registerDropSelfLootTable(n.ToBlock()));
		registerDropSelfLootTable(FABlocks.campfire.ToBlock());
		registerDropSelfLootTable(FABlocks.brickMakerFrame.ToBlock());
		registerDropSelfLootTable(FABlocks.ironCharcoalMix.ToBlock());

		// metal blocks
		for (Metals metal : Metals.values())
		{
			if (metal != Metals.IRON && metal != Metals.GOLD)
				registerDropSelfLootTable(FABlocks.metalBlock.GetBlock(metal));
			registerDropSelfLootTable(FABlocks.metalPlateBlock.GetBlock(metal));
		}
		registerDropSelfLootTable(FABlocks.ironPatternedPlateBlock.ToBlock());
		registerDropSelfLootTable(FABlocks.pillarBronze.ToBlock());
		registerDropSelfLootTable(FABlocks.pillarCopper.ToBlock());
		registerDropSelfLootTable(FABlocks.pillarIron.ToBlock());
		registerDropSelfLootTable(FABlocks.pillarMagmaticBrass.ToBlock());
		registerDropSelfLootTable(FABlocks.pillarPigIron.ToBlock());
		registerDropSelfLootTable(FABlocks.pillarSteel.ToBlock());
		registerDropSelfLootTable(FABlocks.pillarTin.ToBlock());
		registerDropSelfLootTable(FABlocks.altpillarBronze.ToBlock());
		registerDropSelfLootTable(FABlocks.altpillarCopper.ToBlock());
		registerDropSelfLootTable(FABlocks.altpillarIron.ToBlock());
		registerDropSelfLootTable(FABlocks.altpillarMagmaticBrass.ToBlock());
		registerDropSelfLootTable(FABlocks.altpillarPigIron.ToBlock());
		registerDropSelfLootTable(FABlocks.altpillarSteel.ToBlock());
		registerDropSelfLootTable(FABlocks.altpillarTin.ToBlock());

		// ores
		for (MetalOres ore : MetalOres.values())
		{
			registerDropSelfLootTable(FABlocks.metalOres.GetBlock(ore));
		}
		for (Ore.Grade grade : Ore.Grade.values())
		{
			registerDropSelfLootTable(FABlocks.limoniteOre.GetBlock(grade));
			registerDropSelfLootTable(FABlocks.magnetiteOre.GetBlock(grade));
		}
		registerLootTable(FABlocks.siliconQuartzOre.ToBlock(), LootTable.builder().addLootPool(
				LootPool.builder().addEntry(ItemLootEntry.builder(FAItems.siliconQuartz))
						.acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE))));
		registerDropSelfLootTable(FABlocks.rock.ToBlock());
		registerDropping(FABlocks.flintRock.ToBlock(), Items.FLINT);

		// workbenches
		registerDropSelfLootTable(FABlocks.stoneWorkbench.ToBlock());
		registerDropSelfLootTable(FABlocks.ironWorkbench.ToBlock());
		registerDropSelfLootTable(FABlocks.chipCreator.ToBlock());

		// no fluids

		// decoration blocks
		registerDropSelfLootTable(FABlocks.bronzeCauldron.ToBlock());
		registerDropSelfLootTable(FABlocks.bronzeFence.ToBlock());
		registerSilkTouch(FABlocks.slagGlass.ToBlock());

		// smelting
		registerDropSelfLootTable(FABlocks.stoneCrucible.ToBlock());
		registerDropSelfLootTable(FABlocks.stoneCastingVessel.ToBlock());
		registerDropSelfLootTable(FABlocks.brickCrucible.ToBlock());
		registerDropSelfLootTable(FABlocks.brickCastingVessel.ToBlock());
		registerDropSelfLootTable(FABlocks.brickFirebox.ToBlock());
		registerDropSelfLootTable(FABlocks.paperBellows.ToBlock());
		registerDropSelfLootTable(FABlocks.leatherBellows.ToBlock());

		// mechanical
		registerDropSelfLootTable(FABlocks.powerShaft.ToBlock());
		registerDropSelfLootTable(FABlocks.gearbox.ToBlock());
		registerDropSelfLootTable(FABlocks.bevelGear.ToBlock());
		registerDropSelfLootTable(FABlocks.jawCrusher.ToBlock());
		registerDropSelfLootTable(FABlocks.creativeMechanicalSource.ToBlock());
		registerDropSelfLootTable(FABlocks.motor.ToBlock());
		registerDropSelfLootTable(FABlocks.handCrank.ToBlock());
		registerDropSelfLootTable(FABlocks.millstone.ToBlock());
		registerDropSelfLootTable(FABlocks.horseEngine.ToBlock());

		// transfer
		registerDropSelfLootTable(FABlocks.pipe.ToBlock());
		registerDropSelfLootTable(FABlocks.pump.ToBlock());

		// resource blocks
		registerDropSelfLootTable(FABlocks.greenSand.ToBlock());
		registerLootTable(FABlocks.charcoalPile.ToBlock(), LootTable.builder().addLootPool(
				LootPool.builder().addEntry(ItemLootEntry.builder(Items.CHARCOAL))
						.acceptFunction(SetCount.builder(RandomValueRange.of(7, 10)))
						.acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE))));
		registerDropSelfLootTable(FABlocks.logPile.ToBlock());
		registerDropSelfLootTable(FABlocks.terraclayBrickBlock.ToBlock());
		registerDropSelfLootTable(FABlocks.terraclayBlock.ToBlock());
		registerLootTable(FABlocks.ironBloom.ToBlock(), LootTable.builder().addLootPool(
				LootPool.builder().addEntry(ItemLootEntry.builder(FAItems.ironShard))
						.acceptFunction(SetCount.builder(RandomValueRange.of(2, 4)))
						.acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE))).addLootPool(
				LootPool.builder().addEntry(ItemLootEntry.builder(FAItems.slag))
						.acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE))));
	}
}
