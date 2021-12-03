package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.ToolType;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class PillarAlt extends FABaseBlock
{
	final static BooleanProperty TOP = BooleanProperty.create("top");
	final static BooleanProperty BOTTOM = BooleanProperty.create("bottom");

	public PillarAlt(String name, Metals metal)
	{
		super(name, false, Properties.of(Material.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1)
									 .strength(1, 10), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
		registerDefaultState(stateDefinition.any().setValue(TOP, false).setValue(BOTTOM, false));
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific face passed in.
	 */
	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level,
			BlockPos pos, BlockPos facingPos)
	{
		switch (facing)
		{
		case DOWN:
			return state.setValue(BOTTOM, level.getBlockState(pos.below()).getBlock() == this);
		case UP:
			return state.setValue(TOP, level.getBlockState(pos.above()).getBlock() == this);
		default:
			return state;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(TOP, BOTTOM);
	}
}
