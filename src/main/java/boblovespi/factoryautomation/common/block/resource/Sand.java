package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public class Sand extends SandBlock
{
	private final String name;

	public Sand(String name, int particleColor, Properties properties, Item.Properties itemProperties)
	{
		super(particleColor, properties);
		this.name = name;
		// setRegistryName(RegistryName());
		FABlocks.blocks.add(RegistryObjectWrapper.Block(name, this));
		FAItems.items.add(RegistryObjectWrapper.Item(name,new FAItemBlock(this, itemProperties)));
	}

	@Override
	public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_)
	{
		return super.getDrops(p_220076_1_, p_220076_2_);
	}
}
