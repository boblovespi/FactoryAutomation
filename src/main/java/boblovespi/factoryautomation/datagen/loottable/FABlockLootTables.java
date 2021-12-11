package boblovespi.factoryautomation.datagen.loottable;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.RiceCrop;
import boblovespi.factoryautomation.common.block.resource.Ore;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class FABlockLootTables extends BlockLoot
{
	@Override
	protected void addTables()
	{
		super.addTables();
		// multiblock controllers
		dropSelf(FABlocks.steelmakingFurnaceController.ToBlock());
		dropSelf(FABlocks.blastFurnaceController.ToBlock());
		dropSelf(FABlocks.tripHammerController.ToBlock());
		dropSelf(FABlocks.waterwheel.ToBlock());

		// misc
		dropSelf(FABlocks.concrete.ToBlock());
		add(FABlocks.riceCrop.ToBlock(),
				LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(FAItems.riceGrain)))
						.withPool(LootPool.lootPool().when(
								LootItemBlockStatePropertyCondition.hasBlockStateProperties(FABlocks.riceCrop.ToBlock()).setProperties(
										StatePropertiesPredicate.Builder.properties().hasProperty(RiceCrop.AGE, 7)))
										  .add(LootItem.lootTableItem(FAItems.riceGrain))
										  .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3)))
										  .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
		add(FABlocks.concreteSlab, createSlabItemTable(FABlocks.concreteSlab));
		// handle multiblock part separately
		add(FABlocks.multiblockPart.ToBlock(), noDrop());
		dropSelf(FABlocks.cable.ToBlock());
		dropSelf(FABlocks.solarPanel.ToBlock());
		dropSelf(FABlocks.treetap.ToBlock());
		// handle bucket separately
		add(FABlocks.placedBucket.ToBlock(), noDrop());
		dropSelf(FABlocks.factorySign.ToBlock());
		dropSelf(FABlocks.solidfueledfirebox.ToBlock());
		FABlocks.woodChoppingBlocks.forEach(n -> dropSelf(n.ToBlock()));
		dropSelf(FABlocks.campfire.ToBlock());
		dropSelf(FABlocks.brickMakerFrame.ToBlock());
		dropSelf(FABlocks.ironCharcoalMix.ToBlock());

		// metal blocks
		for (Metals metal : Metals.values())
		{
			if (metal != Metals.IRON && metal != Metals.GOLD)
				dropSelf(FABlocks.metalBlock.GetBlock(metal));
			dropSelf(FABlocks.metalPlateBlock.GetBlock(metal));
		}
		dropSelf(FABlocks.ironPatternedPlateBlock.ToBlock());
		dropSelf(FABlocks.pillarBronze.ToBlock());
		dropSelf(FABlocks.pillarCopper.ToBlock());
		dropSelf(FABlocks.pillarIron.ToBlock());
		dropSelf(FABlocks.pillarMagmaticBrass.ToBlock());
		dropSelf(FABlocks.pillarPigIron.ToBlock());
		dropSelf(FABlocks.pillarSteel.ToBlock());
		dropSelf(FABlocks.pillarTin.ToBlock());
		dropSelf(FABlocks.altpillarBronze.ToBlock());
		dropSelf(FABlocks.altpillarCopper.ToBlock());
		dropSelf(FABlocks.altpillarIron.ToBlock());
		dropSelf(FABlocks.altpillarMagmaticBrass.ToBlock());
		dropSelf(FABlocks.altpillarPigIron.ToBlock());
		dropSelf(FABlocks.altpillarSteel.ToBlock());
		dropSelf(FABlocks.altpillarTin.ToBlock());

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
		add(FABlocks.siliconQuartzOre.ToBlock(), LootTable.lootTable().withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(FAItems.siliconQuartz))
						.apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
		dropSelf(FABlocks.rock.ToBlock());
		dropOther(FABlocks.flintRock.ToBlock(), Items.FLINT);

		// workbenches
		dropSelf(FABlocks.stoneWorkbench.ToBlock());
		dropSelf(FABlocks.ironWorkbench.ToBlock());
		dropSelf(FABlocks.chipCreator.ToBlock());

		// no fluids

		// decoration blocks
		dropSelf(FABlocks.bronzeCauldron.ToBlock());
		dropSelf(FABlocks.bronzeFence.ToBlock());
		dropWhenSilkTouch(FABlocks.slagGlass.ToBlock());

		// smelting
		dropSelf(FABlocks.stoneCrucible.ToBlock());
		dropSelf(FABlocks.stoneCastingVessel.ToBlock());
		dropSelf(FABlocks.brickCrucible.ToBlock());
		dropSelf(FABlocks.brickCastingVessel.ToBlock());
		dropSelf(FABlocks.brickFirebox.ToBlock());
		dropSelf(FABlocks.paperBellows.ToBlock());
		dropSelf(FABlocks.leatherBellows.ToBlock());

		// mechanical
		dropSelf(FABlocks.powerShaft.ToBlock());
		dropSelf(FABlocks.gearbox.ToBlock());
		dropSelf(FABlocks.bevelGear.ToBlock());
		dropSelf(FABlocks.jawCrusher.ToBlock());
		dropSelf(FABlocks.creativeMechanicalSource.ToBlock());
		dropSelf(FABlocks.motor.ToBlock());
		dropSelf(FABlocks.handCrank.ToBlock());
		dropSelf(FABlocks.millstone.ToBlock());
		dropSelf(FABlocks.horseEngine.ToBlock());

		// transfer
		dropSelf(FABlocks.pipe.ToBlock());
		dropSelf(FABlocks.pump.ToBlock());

		// resource blocks
		dropSelf(FABlocks.greenSand.ToBlock());
		add(FABlocks.charcoalPile.ToBlock(), LootTable.lootTable().withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(Items.CHARCOAL))
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 10)))
						.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
		dropSelf(FABlocks.logPile.ToBlock());
		dropSelf(FABlocks.terraclayBrickBlock.ToBlock());
		dropSelf(FABlocks.terraclayBlock.ToBlock());
		add(FABlocks.ironBloom.ToBlock(), LootTable.lootTable().withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(FAItems.ironShard))
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
						.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))).withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(FAItems.slag))
						.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
		dropSelf(FABlocks.blackSand.ToBlock());
	}
}
