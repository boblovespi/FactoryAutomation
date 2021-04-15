package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.resource.Rock;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import static boblovespi.factoryautomation.common.block.resource.Rock.VARIANTS;

/**
 * Todo: transform to 1.16.5
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RockBlockPlacer extends BlockPlacer
{
	public static final BlockPlacerType<?> TYPE = BlockPlacerType.register("rock_block_placer", RockBlockPlacer::new);

	public RockBlockPlacer()
	{
		super();
	}

	public RockBlockPlacer(Dynamic<?> unused)
	{
		this();
	}

	@Override
	public void place(IWorld level, BlockPos pos, BlockState state, Random random)
	{
		Biome biome = level.getBiome(pos);
		if (biome.getBiomeCategory() == Biome.Category.DESERT)
		{
			level.setBlock(pos, state.setValue(VARIANTS, Rock.Variants.SANDSTONE), 2);
		} else if (biome.getBiomeCategory() == Biome.Category.SWAMP || biome.getBiomeCategory() == Biome.Category.TAIGA)
		{
			level.setBlock(pos, state.setValue(VARIANTS, Rock.Variants.MOSSY_COBBLESTONE), 2);
		} else
		{
			level.setBlock(pos, state.setValue(VARIANTS, Rock.Variants.values()[random.nextInt(5)]), 2);
		}
	}

	@Override
	public <T> T serialize(DynamicOps<T> dynamicOps)
	{
		return (new Dynamic<>(dynamicOps, dynamicOps.createMap(ImmutableMap.of(
				dynamicOps.createString("type"),
				dynamicOps.createString(Registry.BLOCK_PLACER_TYPE.getKey(this.field_227258_a_).toString())))))
				.getValue();
	}

	@Override
	protected BlockPlacerType<?> type() {
		return TYPE;
	}
}
