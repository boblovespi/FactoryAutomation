package boblovespi.factoryautomation.common.item.Tools;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FABaseItem;
import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class ItemModAxe extends ItemTool implements FAItem
{
	private final String unlocalizedName;
	private static final Set<Block> EFFECTIVE_ON = Sets
			.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG,
					Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN,
					Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER,
					Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);
	private static final float[] ATTACK_DAMAGES = new float[] { 6.0F, 8.0F,
			8.0F, 8.0F, 6.0F };
	private static final float[] ATTACK_SPEEDS = new float[] { -3.2F, -3.2F,
			-3.1F, -3.0F, -3.0F };

	public ItemModAxe(ToolMaterial material, String unlocalizedName)
	{

		super(material, EFFECTIVE_ON);
		this.unlocalizedName = unlocalizedName;
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName(
				new ResourceLocation(FactoryAutomation.MODID, unlocalizedName));
		FAItems.items.add(this);
	}

	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		Material material = state.getMaterial();
		return material != Material.WOOD && material != Material.PLANTS
				&& material != Material.VINE ?
				super.getStrVsBlock(stack, state) :
				this.efficiencyOnProperMaterial;
	}


	@Override
	public String UnlocalizedName()
	{
		return unlocalizedName;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return UnlocalizedName();
	}

	@Override
	public Item ToItem()
	{
		return this;
	}
}
