package boblovespi.factoryautomation.common.block.fluid;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 4/25/2018.
 */
public class FluidFinite extends LiquidBlock implements FABlock
{
	private final String name;

	public FluidFinite(Supplier<FlowingFluid> fluid, Material material, String name)
	{
		super(fluid, Properties.of(material).noDrops().noCollission());
		this.name = name;
		// setUnlocalizedName(UnlocalizedName());
		setRegistryName(FactoryAutomation.MODID, RegistryName());
		FABlocks.blocks.add(this);
		FAItems.items.add(new FAItemBlock(this, new Item.Properties()));
	}

	@Override
	public String UnlocalizedName()
	{
		return name;
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}
}
