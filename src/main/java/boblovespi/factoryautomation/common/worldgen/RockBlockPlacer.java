package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.resource.Rock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.blockplacer.BlockPlacer;
import net.minecraft.world.gen.blockplacer.BlockPlacerType;

import java.util.Random;

import static boblovespi.factoryautomation.common.block.resource.Rock.VARIANTS;

public class RockBlockPlacer extends BlockPlacer
{
	public static final BlockPlacerType<?> TYPE = BlockPlacerType.register("rock_block_placer", RockBlockPlacer::new);

	@Override
	public void place(IWorld world, BlockPos pos, BlockState state, Random random)
	{
		Biome biome = world.getBiome(pos);
		if (biome.getCategory() == Biome.Category.DESERT)
		{
			world.setBlockState(pos, state.with(VARIANTS, Rock.Variants.SANDSTONE), 2);
		} else if (biome.getCategory() == Biome.Category.SWAMP || biome.getCategory() == Biome.Category.TAIGA)
		{
			world.setBlockState(pos, state.with(VARIANTS, Rock.Variants.MOSSY_COBBLESTONE), 2);
		} else
		{
			world.setBlockState(pos, state.with(VARIANTS, Rock.Variants.values()[random.nextInt(5)]), 2);
		}
	}

	@Override
	protected BlockPlacerType<?> getBlockPlacerType()
	{
		return TYPE;
	}

	//	@Override
	//	public <T> T serialize(DynamicOps<T> dynamicOps)
	//	{
	//		return (new Dynamic<>(dynamicOps, dynamicOps.createMap(ImmutableMap.of(
	//				dynamicOps.createString("type"),
	//				dynamicOps.createString(Registry.BLOCK_PLACER_TYPE.getKey(this.field_227258_a_).toString())))))
	//				.getValue();
	//	}
}
