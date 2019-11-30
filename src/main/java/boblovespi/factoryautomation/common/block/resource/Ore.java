package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.MultiTypeBlock;
import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.ToolType;

/**
 * Created by Willi on 4/1/2018.
 * base ore class
 */
public class Ore extends MultiTypeBlock<Ore.Grade>
{
	public Ore(String name, int harvestLevel, Properties properties, Item.Properties itemProperties)
	{
		super(
				name, Ore.Grade.class, "ores", properties.harvestTool(ToolType.PICKAXE).harvestLevel(harvestLevel),
				itemProperties);
	}

	//	@Override
	//	@SideOnly(Side.CLIENT)
	//	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	//	{
	//		// String grade = NBTHelper.GetTag(stack).getCompoundTag("blockdata").getString("grade");
	//		// tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.grade") + ": " + StringUtils.capitalize(grade));
	//	}

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
