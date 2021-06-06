package boblovespi.factoryautomation.datagen.loottable;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.*;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FALootTableProvider extends LootTableProvider
{
	private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> tables = new ArrayList<>();

	public FALootTableProvider(DataGenerator dataGeneratorIn)
	{
		super(dataGeneratorIn);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
	{
		tables.addAll(ImmutableList.of(
				Pair.of(FishingLootTables::new, LootParameterSets.FISHING),
				Pair.of(ChestLootTables::new, LootParameterSets.CHEST),
				Pair.of(EntityLootTables::new, LootParameterSets.ENTITY),
				Pair.of(GiftLootTables::new, LootParameterSets.GIFT)));
		tables.add(Pair.of(FABlockLootTables::new, LootParameterSets.BLOCK));
		return tables;
	}
}
