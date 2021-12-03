package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.resource.Rock;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.blockplacers.BlockPlacer;
import net.minecraft.world.level.levelgen.feature.blockplacers.BlockPlacerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;
import static boblovespi.factoryautomation.common.block.resource.Rock.VARIANTS;

/**
 * Todo: transform to 1.16.5
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RockBlockPlacer extends BlockPlacer
{
	public static final BlockPlacer INSTANCE = new RockBlockPlacer();
	public static final Codec<BlockPlacer> CODEC = Codec.unit(() -> INSTANCE);
	public static final DeferredRegister<BlockPlacerType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_PLACER_TYPES, MODID);
	public static final RegistryObject<BlockPlacerType<BlockPlacer>> TYPE = REGISTRY.register("rock_block_placer", () -> new BlockPlacerType<>(CODEC));
/*	@Override
	public <T> T serialize(DynamicOps<T> dynamicOps)
	{
		return (new Dynamic<>(dynamicOps, dynamicOps.createMap(ImmutableMap.of(
				dynamicOps.createString("type"),
				dynamicOps.createString(Registry.BLOCK_PLACER_TYPE_REGISTRY.getKey(this.field_227258_a_).toString())))))
				.getValue();
	}*/

	public RockBlockPlacer()
	{
		super();
	}

	public RockBlockPlacer(Dynamic<?> unused)
	{
		this();
	}

	@Override
	public void place(LevelAccessor level, BlockPos pos, BlockState state, Random random)
	{
		Biome biome = level.getBiome(pos);
		if (biome.getBiomeCategory() == Biome.BiomeCategory.DESERT)
		{
			level.setBlock(pos, state.setValue(VARIANTS, Rock.Variants.SANDSTONE), 2);
		} else if (biome.getBiomeCategory() == Biome.BiomeCategory.SWAMP || biome.getBiomeCategory() == Biome.BiomeCategory.TAIGA)
		{
			level.setBlock(pos, state.setValue(VARIANTS, Rock.Variants.MOSSY_COBBLESTONE), 2);
		} else
		{
			level.setBlock(pos, state.setValue(VARIANTS, Rock.Variants.values()[random.nextInt(5)]), 2);
		}
	}

	@Override
	protected BlockPlacerType<?> type()
	{
		return TYPE.get();
	}
}
