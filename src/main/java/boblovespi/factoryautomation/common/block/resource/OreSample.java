package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 12/25/2018.
 */
@SuppressWarnings("deprecation")
public class OreSample extends FABaseBlock
{
	private static final AABB BOUNDING_BOX = new AABB(
			3 / 16d, 0, 3 / 16d, 13 / 16d, 5 / 16d, 13 / 16d);
	private static final VoxelShape BOUNDING_BOX_VS = Shapes.create(BOUNDING_BOX);
	private final ItemStack[] possibleDrops; // the drops the "rock" can drop

	public OreSample(String name, ItemStack[] possibleDrops)
	{
		super(name, true, Properties.of(Materials.ROCKS).strength(0.1f), FAItems.Prop());
		this.possibleDrops = possibleDrops;
		// setLightOpacity(0);
		// setHardness(0.1f);
	}

	//	@Override
	//	public boolean isNormalCube(BlockState state, IBlockReader level, BlockPos pos)
	//	{
	//		return false;
	//	}

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
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return BOUNDING_BOX_VS;
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos,
								boolean isMoving)
	{
		if (!world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), Direction.UP)) // isSideSolid ?
		{
			dropResources(state, world, pos);
			world.removeBlock(pos, isMoving);
		}
	}

	/**
	 * Checks if this block can be placed exactly at the given position.
	 */
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos)
	{
		return world.getBlockState(pos).getMaterial().isReplaceable() && world.getBlockState(pos.below())
																				 .isFaceSturdy(world, pos.below(),
																						 Direction.UP)
					   && world.getBlockState(pos).getBlock() != this;
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos,
									   Player player)
	{
		return possibleDrops[0].copy();
	}
}
