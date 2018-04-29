package boblovespi.factoryautomation.common.block.fluid;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;

/**
 * Created by Willi on 4/25/2018.
 */
public class FluidFinite extends BlockFluidFinite implements FABlock
{
	private final String name;

	public FluidFinite(Fluid fluid, Material material, String name)
	{
		super(fluid, material);
		this.name = name;
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(FactoryAutomation.MODID, RegistryName());
		FABlocks.blocks.add(this);
		FAItems.items.add(new FAItemBlock(this));
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
