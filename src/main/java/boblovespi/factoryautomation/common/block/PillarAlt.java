package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;

public class PillarAlt extends FABaseBlock
{
	final static BooleanProperty TOP = BooleanProperty.create("top");
	final static BooleanProperty BOTTOM = BooleanProperty.create("bottom");

	public PillarAlt(String name, Metals metal)
	{
		super(name, false, Properties.of(Material.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1)
									 .strength(1, 10), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS));
		registerDefaultState(stateDefinition.getBaseState().with(TOP, false).with(BOTTOM, false));
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific face passed in.
	 */
	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world,
			BlockPos pos, BlockPos facingPos)
	{
		switch (facing)
		{
		case DOWN:
			return state.with(BOTTOM, world.getBlockState(pos.down()).getBlock() == this);
		case UP:
			return state.with(TOP, world.getBlockState(pos.up()).getBlock() == this);
		default:
			return state;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(TOP, BOTTOM);
	}
}
