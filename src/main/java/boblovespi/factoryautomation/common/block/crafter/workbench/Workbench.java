package boblovespi.factoryautomation.common.block.crafter.workbench;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.FACE;
import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;

/**
 * Created by Willi on 4/15/2018.
 */
public abstract class Workbench extends FABaseBlock
{
	public Workbench(Material material, String name)
	{
		super(name, false, Properties.of(material).strength(2),
				new Item.Properties().tab(FAItemGroups.crafting));
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		return "workbench/" + registryName();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(HORIZONTAL_FACING);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return getDefaultState().with(HORIZONTAL_FACING, context.getHorizontalDirection());
	}
}
