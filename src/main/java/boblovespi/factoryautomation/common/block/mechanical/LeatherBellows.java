package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TELeatherBellows;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * Created by Willi on 5/5/2019.
 */
public class LeatherBellows extends FABaseBlock
{
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

	public LeatherBellows()
	{
		super("leather_bellows", false,
				Properties.create(Material.WOOD).hardnessAndResistance(0.5f).sound(SoundType.CLOTH),
				new Item.Properties().group(FAItemGroups.mechanical));
		TileEntityHandler.tiles.add(TELeatherBellows.class);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TELeatherBellows();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
}
