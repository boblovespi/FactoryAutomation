package boblovespi.factoryautomation.common.block.crafter.workbench;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.StateDefinition;

import javax.annotation.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class Workbench extends FABaseBlock
{
	public Workbench(Material material, String name)
	{
		super(name, false, Properties.of(material).strength(2),
				new Item.Properties().tab(FAItemGroups.crafting));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(HORIZONTAL_FACING);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection());
	}
}
