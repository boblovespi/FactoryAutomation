package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.smelting.TEPaperBellows;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 5/5/2019.
 */
public class PaperBellows extends FABaseBlock
{
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

	public PaperBellows()
	{
		super("paper_bellows", false,
				Properties.of(Material.WOOL).strength(0.5f).sound(SoundType.CLOTH),
				new Item.Properties().tab(FAItemGroups.primitive));
		TileEntityHandler.tiles.add(TEPaperBellows.class);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit)
	{
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TEPaperBellows)
			((TEPaperBellows) te).Blow();
		world.playSound(player, pos, SoundEvents.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.BLOCKS, 0.8f, 1.5f);
		return ActionResultType.SUCCESS;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.INVISIBLE;
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
		return new TEPaperBellows();
	}

	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return VoxelShapes.empty();
	}
}
