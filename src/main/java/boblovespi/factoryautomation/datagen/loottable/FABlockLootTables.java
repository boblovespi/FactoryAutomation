package boblovespi.factoryautomation.datagen.loottable;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.RiceCrop;
import boblovespi.factoryautomation.common.block.resource.Ore;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.ores.OreForms;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class FABlockLootTables extends BlockLoot
{
	private static LootTable.Builder CreateMultiOreDrop(Block block, Item item, int quantity)
	{
		return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(item)
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(quantity)))
				.apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
	}

	@Override
	protected void addTables()
	{
		super.addTables();
		// multiblock controllers
		dropSelf(FABlocks.steelmakingFurnaceController);
		dropSelf(FABlocks.blastFurnaceController);
		dropSelf(FABlocks.tripHammerController);
		dropSelf(FABlocks.waterwheel);

		// misc
		dropSelf(FABlocks.concrete);
		add(FABlocks.riceCrop,
			LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(FAItems.riceGrain)))
					.withPool(LootPool.lootPool().when(
									LootItemBlockStatePropertyCondition.hasBlockStateProperties(FABlocks.riceCrop)
											.setProperties(
													StatePropertiesPredicate.Builder.properties().hasProperty(RiceCrop.AGE, 7)))
									  .add(LootItem.lootTableItem(FAItems.riceGrain))
									  .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3)))
									  .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
		add(FABlocks.concreteSlab, createSlabItemTable(FABlocks.concreteSlab));
		// handle multiblock part separately
		add(FABlocks.multiblockPart, noDrop());
		dropSelf(FABlocks.cable);
		dropSelf(FABlocks.solarPanel);
		dropSelf(FABlocks.treetap);
		// handle bucket separately
		add(FABlocks.placedBucket, noDrop());
		dropSelf(FABlocks.factorySign);
		dropSelf(FABlocks.solidfueledfirebox);
		FABlocks.woodChoppingBlocks.forEach(n -> dropSelf(n));
		dropSelf(FABlocks.campfire);
		dropSelf(FABlocks.brickMakerFrame);
		dropSelf(FABlocks.ironCharcoalMix);

		// metal blocks
		for (Metals metal : Metals.values())
		{
			if (metal != Metals.IRON && metal != Metals.GOLD && metal != Metals.COPPER)
				dropSelf(FABlocks.metalBlock.GetBlock(metal));
			dropSelf(FABlocks.metalPlateBlock.GetBlock(metal));
		}
		dropSelf(FABlocks.ironPatternedPlateBlock);
		dropSelf(FABlocks.pillarBronze);
		dropSelf(FABlocks.pillarCopper);
		dropSelf(FABlocks.pillarIron);
		dropSelf(FABlocks.pillarMagmaticBrass);
		dropSelf(FABlocks.pillarPigIron);
		dropSelf(FABlocks.pillarSteel);
		dropSelf(FABlocks.pillarTin);
		dropSelf(FABlocks.altpillarBronze);
		dropSelf(FABlocks.altpillarCopper);
		dropSelf(FABlocks.altpillarIron);
		dropSelf(FABlocks.altpillarMagmaticBrass);
		dropSelf(FABlocks.altpillarPigIron);
		dropSelf(FABlocks.altpillarSteel);
		dropSelf(FABlocks.altpillarTin);

		// ores
		add(FABlocks.metalOres.GetBlock(MetalOres.TIN), createOreDrop(FABlocks.metalOres.GetBlock(MetalOres.TIN), FAItems.processedCassiterite.GetItem(OreForms.CHUNK)));
		dropSelf(FABlocks.metalOres.GetBlock(MetalOres.COPPER));

		for (Ore.Grade grade : Ore.Grade.values())
		{
			add(FABlocks.limoniteOre.GetBlock(grade), CreateMultiOreDrop(FABlocks.limoniteOre.GetBlock(grade), FAItems.processedLimonite.GetItem(OreForms.CHUNK), grade.quantity));
			dropSelf(FABlocks.magnetiteOre.GetBlock(grade));
		}
		add(FABlocks.siliconQuartzOre, LootTable.lootTable().withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(FAItems.siliconQuartz))
						.apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
		dropSelf(FABlocks.rock);
		dropOther(FABlocks.flintRock, Items.FLINT);

		// workbenches
		dropSelf(FABlocks.stoneWorkbench);
		dropSelf(FABlocks.ironWorkbench);
		dropSelf(FABlocks.chipCreator);

		// no fluids

		// decoration blocks
		dropSelf(FABlocks.bronzeCauldron);
		dropSelf(FABlocks.bronzeFence);
		dropWhenSilkTouch(FABlocks.slagGlass);

		// smelting
		dropSelf(FABlocks.stoneCrucible);
		dropSelf(FABlocks.stoneCastingVessel);
		dropSelf(FABlocks.brickCrucible);
		dropSelf(FABlocks.brickCastingVessel);
		dropSelf(FABlocks.brickFirebox);
		dropSelf(FABlocks.paperBellows);
		dropSelf(FABlocks.leatherBellows);

		// mechanical
		dropSelf(FABlocks.powerShaft);
		dropSelf(FABlocks.powerShaftWood);

		dropSelf(FABlocks.gearbox);
		dropSelf(FABlocks.bevelGear);
		dropSelf(FABlocks.splitterIron);

		dropSelf(FABlocks.jawCrusher);
		dropSelf(FABlocks.creativeMechanicalSource);
		dropSelf(FABlocks.motor);
		dropSelf(FABlocks.handCrank);
		dropSelf(FABlocks.millstone);
		dropSelf(FABlocks.horseEngine);
		dropSelf(FABlocks.screwPump);
		dropSelf(FABlocks.tumblingBarrel);

		// transfer
		dropSelf(FABlocks.ironPipe);
		dropSelf(FABlocks.woodPipe);
		dropSelf(FABlocks.pump);
		dropSelf(FABlocks.woodenTank);

		// resource blocks
		dropSelf(FABlocks.greenSand);
		add(FABlocks.charcoalPile, LootTable.lootTable().withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(Items.CHARCOAL))
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 10)))
						.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
		dropSelf(FABlocks.logPile);
		dropSelf(FABlocks.terraclayBrickBlock);
		dropSelf(FABlocks.terraclayBlock);
		add(FABlocks.ironBloom, LootTable.lootTable().withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(FAItems.ironShard))
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
						.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))).withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(FAItems.slag))
						.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
		dropSelf(FABlocks.blackSand);

		dropSelf(FABlocks.limoniteRawBlock);
		dropSelf(FABlocks.cassiteriteRawBlock);
	}
}
