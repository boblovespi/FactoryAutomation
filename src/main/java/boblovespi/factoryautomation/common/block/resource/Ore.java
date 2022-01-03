package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.MultiTypeBlock;
import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Created by Willi on 4/1/2018.
 * base ore class
 */
public class Ore extends MultiTypeBlock<Ore.Grade>
{
	public Ore(String name, int harvestLevel, BlockBehaviour.Properties properties, Item.Properties itemProperties)
	{
		super(
				name, Ore.Grade.class, "ores", properties.requiresCorrectToolForDrops(),
				itemProperties);
	}

	//	@Override
	//	@SideOnly(Side.CLIENT)
	//	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	//	{
	//		// String grade = NBTHelper.GetTag(stack).getCompoundTag("blockdata").getString("grade");
	//		// tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.grade") + ": " + StringUtils.capitalize(grade));
	//	}

	public enum Grade implements StringRepresentable, IMultiTypeEnum
	{
		POOR(0, "poor", 1), NORMAL(1, "normal", 2), RICH(2, "rich", 3);

		private final int id;
		private final String name;
		public final int quantity;

		Grade(int id, String name, int quantity)
		{
			this.id = id;
			this.name = name;
			this.quantity = quantity;
		}

		@Override
		public String getSerializedName()
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
