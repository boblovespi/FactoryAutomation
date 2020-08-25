package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEHandCrank;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 9/3/2018.
 */
public class HandCrank extends FABaseBlock
{
	public static final BooleanProperty INVERTED = BooleanProperty.create("inverted");
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(
			2 / 16d, 0, 2 / 16d, 14 / 16d, 14 / 16d, 14 / 16d);
	private static final AxisAlignedBB BOUNDING_BOX_I = new AxisAlignedBB(
			2 / 16d, 2 / 16d, 2 / 16d, 14 / 16d, 1, 14 / 16d);

	public HandCrank()
	{
		super("hand_crank", false, Properties.create(Material.WOOD).hardnessAndResistance(1.3f),
				new Item.Properties().group(FAItemGroups.mechanical));
		TileEntityHandler.tiles.add(TEHandCrank.class);
		setDefaultState(stateContainer.getBaseState().with(INVERTED, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return context.getFace() == Direction.DOWN ? getDefaultState().with(INVERTED, true) : getDefaultState();
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
		return new TEHandCrank();
	}

	/**
	 * Called when the block is right clicked by a player.
	 * @return
	 */
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit)
	{
		if (!world.isRemote)
		{
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TEHandCrank)
			{
				TEHandCrank crank = (TEHandCrank) tileEntity;

				crank.Rotate();
				player.addExhaustion(0.8f);
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(INVERTED);
	}
}
