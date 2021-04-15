package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by Willi on 1/27/2019.
 */
public class LogPile extends FABaseBlock
{
	public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");
	private static final VoxelShape BOUNDING_BOX = VoxelShapes
			.or(Block.box(0, 0, 0, 4, 16, 4), Block.box(6, 0, 0, 10, 16, 4),
					Block.box(12, 0, 0, 16, 16, 4), Block.box(0, 0, 6, 4, 16, 10),
					Block.box(6, 0, 6, 10, 16, 10), Block.box(12, 0, 6, 16, 16, 10),
					Block.box(0, 0, 12, 4, 16, 16), Block.box(6, 0, 12, 10, 16, 16),
					Block.box(12, 0, 12, 16, 16, 16)).optimize();

	public LogPile()
	{
		super("log_pile", false,
				Properties.of(Material.WOOD).strength(1.2f).harvestLevel(0).harvestTool(ToolType.AXE),
				new Item.Properties().tab(FAItemGroups.primitive));
		registerDefaultState(stateDefinition.any().with(ACTIVATED, false));
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return BOUNDING_BOX;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face)
	{
		return state.getValue(ACTIVATED) ? 20 : 8;
	}

	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face)
	{
		return state.getValue(ACTIVATED) ? 120 : 20;
	}

	@Override
	public int tickRate(IWorldReader worldIn)
	{
		return 6000;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
	{
		boolean activated = state.getValue(ACTIVATED);
		if (activated)
		{
			world.setBlockState(pos, FABlocks.charcoalPile.ToBlock().getDefaultState());
			for (Direction dir : Direction.values())
			{
				BlockPos offset = pos.offset(dir);
				BlockState state1 = world.getBlockState(offset);
				if (state1.getBlock() == FABlocks.terraclayBrickBlock)
				{
					boolean foundPile = false;
					for (int i = 4; i >= 0; i--)
					{
						BlockPos pos1 = offset.offset(dir, i);
						BlockState state2 = world.getBlockState(pos1);
						if (foundPile)
						{
							if (state2.getBlock() == FABlocks.terraclayBrickBlock && isSurrounded(world, pos1, null))
								world.setBlockState(pos1, Blocks.BRICKS.getDefaultState());
						} else if (state2.getBlock() == this && state2.get(ACTIVATED))
							foundPile = true;
					}
				}
			}
		}
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block1, BlockPos fromPos,
			boolean isMoving)
	{
		BlockState block = world.getBlockState(fromPos);
		if (!state.getValue(ACTIVATED))
		{
			if (block.getBlock() == Blocks.FIRE || (block.getBlock() == this && block.get(ACTIVATED)))
			{
				world.setBlockState(pos, state.setValue(ACTIVATED, true), 7);
				world.getPendingBlockTicks().scheduleTick(pos, this, tickRate(world));
			}

		} else
		{
			boolean sidesOnFire = false;
			boolean isSurrounded = true;
			for (Direction face : Direction.values())
			{
				BlockPos offset = pos.offset(face);
				BlockState state1 = world.getBlockState(offset);
				if (state1.getBlock().isAir(state1, world, offset))
				{
					world.setBlockState(offset, Blocks.FIRE.getDefaultState());
					sidesOnFire = true;
				} else if (state1.getBlock() == Blocks.FIRE)
				{
					// do something later?
					sidesOnFire = true;
				} else if (!Block.hasSolidSide(state1, world, offset, face.getOpposite()) && state1.getBlock() != this)
				{
					// if (world.rand.nextFloat() < 0.2f)
					// 	world.setBlockState(pos, Blocks.FIRE.getDefaultState());
					isSurrounded = false;
				}
			}
			if (!sidesOnFire && !isSurrounded)
				world.setBlockState(pos, Blocks.FIRE.getDefaultState());
		}
	}

	@Override
	public boolean isBurning(BlockState state, IBlockReader world, BlockPos pos)
	{
		return state.getValue(ACTIVATED);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(ACTIVATED);
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
	{
		if (!state.getValue(ACTIVATED))
			return;
		double x = pos.getX() + rand.nextDouble();
		double y = pos.getY() + rand.nextDouble();
		double z = pos.getZ() + rand.nextDouble();
		world.addParticle(ParticleTypes.LAVA, x, y, z, rand.nextDouble() / 20d, rand.nextDouble() / 20d,
				rand.nextDouble() / 20d);
		world.addParticle(ParticleTypes.SMOKE, x, y + 1.5, z, rand.nextDouble() / 20d, 0.05, rand.nextDouble() / 20d);
		world.addParticle(ParticleTypes.SMOKE, x, y + 1.5, z, rand.nextDouble() / 20d, 0.05, rand.nextDouble() / 20d);
	}

	private boolean isSurrounded(World world, BlockPos pos, @Nullable Predicate<BlockState> block)
	{
		for (Direction dir : Direction.values())
		{
			BlockPos offset = pos.offset(dir);
			BlockState state = world.getBlockState(offset);
			if (block == null)
			{
				if (!Block.hasSolidSide(state, world, offset, dir.getOpposite()) && state.getBlock() != this)
					return false;
			} else
			{
				if (!block.test(state) && state.getBlock() != this)
					return false;
			}
		}
		return true;
	}
}
