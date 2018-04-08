package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.MultiTypeBlock;
import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.IStringSerializable;

/**
 * Created by Willi on 4/1/2018.
 */
public class Ore extends MultiTypeBlock<Ore.Grade>
{
	public Ore(String name, int harvestLevel)
	{
		super(Material.ROCK, name, Grade.class, "ores");
		setHarvestLevel("pickaxe", harvestLevel);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		if (TYPE == null)
			TYPE = PropertyEnum.create("type", Grade.class);
		return super.createBlockState();
	}

	public enum Grade implements IStringSerializable, IMultiTypeEnum
	{
		POOR(0, "poor"), NORMAL(1, "normal"), RICH(2, "rich");

		private final int id;
		private final String name;

		Grade(int id, String name)
		{
			this.id = id;
			this.name = name;
		}

		@Override
		public String getName()
		{
			return name;
		}

		@Override
		public String toString()
		{
			return name;
		}

		@Override
		public int GetId()
		{
			return id;
		}
	}
}
