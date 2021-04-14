package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEJawCrusher;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 2/17/2018.
 */
public class JawCrusher extends FABaseBlock
{
	public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public JawCrusher()
	{
		super("jaw_crusher", false, Properties.of(Material.METAL).strength(1.5f),
				new Item.Properties().tab(FAItemGroups.mechanical));
		registerDefaultState(stateDefinition.getBaseState().with(FACING, Direction.NORTH));
		TileEntityHandler.tiles.add(TEJawCrusher.class);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState meta, IBlockReader worldIn)
	{
		return new TEJawCrusher();
	}

	/**
	 * Called when the block is right clicked by a player.
	 * @return
	 */
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand,
			BlockRayTraceResult result)
	{
		if (worldIn.isRemote)
			return ActionResultType.SUCCESS;

		ItemStack held = playerIn.getHeldItem(hand);
		TileEntity te = worldIn.getTileEntity(pos);
		if (held.isEmpty())
		{
			if (te instanceof TEJawCrusher)
				((TEJawCrusher) te).RemovePlate();
		} else
		{
			if (te instanceof TEJawCrusher)
				((TEJawCrusher) te).PlaceWearPlate(held);
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
}
