package boblovespi.factoryautomation.common.block.fluid;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.Item;

import java.util.function.Supplier;

/**
 * Created by Willi on 4/25/2018.
 */
public class FluidFinite extends FlowingFluidBlock implements FABlock
{
	private final String name;

	public FluidFinite(Supplier<FlowingFluid> fluid, Material material, String name)
	{
		super(fluid, Properties.create(material));
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
