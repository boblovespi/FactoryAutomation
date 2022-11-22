package boblovespi.factoryautomation.common.block.fluid;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

/**
 * Created by Willi on 4/25/2018.
 */
public class FluidFinite extends LiquidBlock
{
	private final String name;

	public FluidFinite(Supplier<FlowingFluid> fluid, Material material, String name)
	{
		super(fluid, Properties.of(material).noLootTable().noCollission());
		this.name = name;
		// setUnlocalizedName(UnlocalizedName());
		// setRegistryName(FactoryAutomation.MODID, RegistryName());
		FABlocks.blocks.add(RegistryObjectWrapper.Block(name, this));
		FAItems.items.add(RegistryObjectWrapper.Item(name, new FAItemBlock(this, new Item.Properties())));
	}
}
