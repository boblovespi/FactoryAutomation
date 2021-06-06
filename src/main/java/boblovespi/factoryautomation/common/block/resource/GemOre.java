package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Random;

/**
 * Created by Willi on 5/24/2018.
 * ores that are gem ores (like diamonds)
 */
public class GemOre extends FABaseBlock
{
	private final OreData data;
	private final Random random;

	public GemOre(String name, OreData data)
	{
		super(name, false, Properties.of(Material.STONE).strength(data.hardness, data.resistance)
									 .harvestLevel(data.miningLevel).harvestTool(ToolType.PICKAXE),
				new Item.Properties().tab(ItemGroup.BUILDING_BLOCKS));
		this.data = data;
		random = new Random();
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(BlockState state, Random rand, int fortune)
	{
		return data.ore;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(Random random)
	{
		return data.dropChance.apply(random);
	}

	/**
	 * Get the quantity dropped based on the given fortune world
	 */
	public int quantityDroppedWithBonus(int fortune, Random random)
	{
		if (fortune > 0 && item != getItemDropped(stateDefinition.any(), random, fortune))
		{
			int i = random.nextInt(fortune + 2) - 1;

			if (i < 0)
			{
				i = 0;
			}

			return this.quantityDropped(random) * (i + 1);
		} else
		{
			return this.quantityDropped(random);
		}
	}

	@Override
	public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch)
	{
		Random rand = world instanceof World ? ((World) world).rand : random;
		return data.xpChance.apply(rand, fortune);
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		return "ores/" + registryName();
	}
}
