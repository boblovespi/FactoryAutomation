package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class Sand extends SandBlock implements FABlock
{
	private final String unlocalizedName;

	public Sand(String name, int particleColor, Properties properties, Item.Properties itemProperties)
	{
		super(particleColor, properties);
		this.unlocalizedName = name;
		setRegistryName(RegistryName());
		FABlocks.blocks.add(this);
		FAItems.items.add(new FAItemBlock(this, itemProperties));
	}

	@Override
	public String UnlocalizedName()
	{
		return unlocalizedName;
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	@Override
	public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_)
	{
		return super.getDrops(p_220076_1_, p_220076_2_);
	}
}
