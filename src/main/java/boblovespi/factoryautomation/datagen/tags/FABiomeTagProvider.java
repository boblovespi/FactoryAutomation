package boblovespi.factoryautomation.datagen.tags;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import static net.minecraftforge.registries.ForgeRegistries.Keys.BIOMES;

public class FABiomeTagProvider extends BiomeTagsProvider
{
	public FABiomeTagProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper)
	{
		super(dataGenerator, FactoryAutomation.MODID, existingFileHelper);
	}

	@Override
	protected void addTags()
	{
		tag(FATags.FATag(BIOMES, "is_red_desert_overworld")).addTags(BiomeTags.IS_BADLANDS, BiomeTags.IS_SAVANNA);
		tag(FATags.FATag(BIOMES, "is_yellow_desert_overworld")).add(Biomes.DESERT);
		tag(FATags.FATag(BIOMES, "is_yellow_desert_overworld")).addTags(BiomeTags.IS_JUNGLE, BiomeTags.IS_TAIGA,
				Tags.Biomes.IS_SWAMP);
		tag(FATags.FATag(BIOMES, "is_typical_overworld")).addTags(BiomeTags.IS_FOREST, BiomeTags.IS_HILL,
				Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_PEAK);
		tag(FATags.FATag(BIOMES, "is_surface_overworld")).addTags(FATags.FATag(BIOMES, "is_red_desert_overworld"),
				FATags.FATag(BIOMES, "is_yellow_desert_overworld"), FATags.FATag(BIOMES, "is_yellow_desert_overworld"),
				FATags.FATag(BIOMES, "is_typical_overworld"));
	}
}
