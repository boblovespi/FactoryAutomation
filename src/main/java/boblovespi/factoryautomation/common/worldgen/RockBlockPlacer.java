package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.resource.Rock;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;

import java.util.Random;

import static boblovespi.factoryautomation.common.block.resource.Rock.VARIANTS;

public class RockBlockPlacer extends BlockPlacer
{
	public static final BlockPlacerType<?> TYPE = BlockPlacerType.register("rock_block_placer", RockBlockPlacer::new);

	public RockBlockPlacer()
	{
		super(TYPE);
	}

	public RockBlockPlacer(Dynamic<?> unused)
	{
		this();
	}

	@Override
	public void func_225567_a_(IWorld world, BlockPos pos, BlockState state, Random random)
	{
		Biome biome = world.getBiome(pos);
		if (biome.getCategory() == Biome.Category.DESERT)
		{
			world.setBlockState(pos, state.setValue(VARIANTS, Rock.Variants.SANDSTONE), 2);
		} else if (biome.getCategory() == Biome.Category.SWAMP || biome.getCategory() == Biome.Category.TAIGA)
		{
			world.setBlockState(pos, state.setValue(VARIANTS, Rock.Variants.MOSSY_COBBLESTONE), 2);
		} else
		{
			world.setBlockState(pos, state.setValue(VARIANTS, Rock.Variants.values()[random.nextInt(5)]), 2);
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
}
