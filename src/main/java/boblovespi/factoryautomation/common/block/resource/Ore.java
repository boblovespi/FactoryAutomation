package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.MultiStateBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.MultiStateItemBlock;
import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import boblovespi.factoryautomation.common.util.NBTHelper;
import com.google.common.base.Optional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

/**
 * Created by Willi on 4/1/2018.
 */
public class Ore extends MultiStateBlock<Ore.Grade>
{
	public Ore(String name, int harvestLevel)
	{
		super(Material.ROCK, name, Grade.class, "ores");
		setHarvestLevel("pickaxe", harvestLevel);
		FAItems.items.add(new MultiStateItemBlock<>(this, Grade.class));
	}

	@Override
	protected void SetType()
	{
		TYPE = PropertyEnum.create("grade", Grade.class);
	}

	@Override
	public NBTTagCompound GetTagFromState(IBlockState state)
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("grade", state.getValue(TYPE).getName());
		return tag;
	}

	@Override
	public IBlockState GetStateFromTag(NBTTagCompound tag)
	{
		String type = tag.getString("grade");
		Optional<Grade> gradeOptional = TYPE.parseValue(type.toUpperCase(Locale.ROOT));
		return blockState.getBaseState().withProperty(TYPE, gradeOptional.or(Grade.POOR));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	{
		String grade = NBTHelper.GetTag(stack).getCompoundTag("blockdata").getString("grade");
		tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.grade") + ": " + StringUtils.capitalize(grade));
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
