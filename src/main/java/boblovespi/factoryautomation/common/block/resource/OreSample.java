package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * Created by Willi on 12/25/2018.
 */
@SuppressWarnings("deprecation")
public class OreSample extends FABaseBlock
{
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(
			3 / 16d, 0, 3 / 16d, 13 / 16d, 5 / 16d, 13 / 16d);
	private static final VoxelShape BOUNDING_BOX_VS = VoxelShapes.create(BOUNDING_BOX);
	private final ItemStack[] possibleDrops; // the drops the "rock" can drop

	public OreSample(String name, ItemStack[] possibleDrops)
	{
		super(name, false, Properties.of(Materials.ROCKS).strength(0.1f), FAItems.Prop());
		this.possibleDrops = possibleDrops;
		// setLightOpacity(0);
		// setHardness(0.1f);
	}

	@Override
	public boolean isNormalCube(BlockState state, IBlockReader level, BlockPos pos)
	{
		return false;
	}

	// TODO: Use loot tables!!!
	//	@Override
	//	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess level, BlockPos pos, BlockState state, int fortune)
	//	{
	//		drops.add(possibleDrops[(int) (Math.random() * possibleDrops.length)].copy());
	//	}

	//	@Override
	//	public boolean canSilkHarvest(World level, BlockPos pos, BlockState state, PlayerEntity player)
	//	{
	//		return false;
	//	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader levelIn, BlockPos pos, ISelectionContext context)
	{
		return BOUNDING_BOX_VS;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "resources/" + RegistryName();
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(BlockState state, World level, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving)
	{
		if (!world.getBlockState(pos.down()).isSolidSide(world, pos.below(), Direction.UP)) // isSideSolid ?
		{
			spawnDrops(state, level, pos);
			world.removeBlock(pos, isMoving);
		}
	}

	/**
	 * Checks if this block can be placed exactly at the given position.
	 */
	@Override
	public boolean canSurvive(BlockState state, IWorldReader level, BlockPos pos)
	{
		return level.getBlockState(pos).getMaterial().isReplaceable() && level.getBlockState(pos.down())
																			  .isSolidSide(world, pos.below(),
																					  Direction.UP)
				&& level.getBlockState(pos).getBlock() != this;
	}
}
