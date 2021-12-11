package boblovespi.factoryautomation.datagen.loottable;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FALootTableProvider extends LootTableProvider
{
	private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables = new ArrayList<>();

	public FALootTableProvider(DataGenerator dataGeneratorIn)
	{
		super(dataGeneratorIn);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables()
	{
		tables.addAll(ImmutableList.of(
				Pair.of(FishingLoot::new, LootContextParamSets.FISHING),
				Pair.of(ChestLoot::new, LootContextParamSets.CHEST),
				Pair.of(EntityLoot::new, LootContextParamSets.ENTITY),
				Pair.of(PiglinBarterLoot::new, LootContextParamSets.PIGLIN_BARTER),
				Pair.of(GiftLoot::new, LootContextParamSets.GIFT)));
		tables.add(Pair.of(FABlockLootTables::new, LootContextParamSets.BLOCK));
		return tables;
	}
}
