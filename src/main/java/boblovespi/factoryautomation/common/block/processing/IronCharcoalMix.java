package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by Willi on 2/3/2019.
 */
public class IronCharcoalMix extends FABaseBlock
{
	public static final BooleanProperty ACTIVATED = LogPile.ACTIVATED;

	public IronCharcoalMix()
	{
		super("iron_charcoal_mix", false, Properties.create(Material.ROCK).hardnessAndResistance(3.5f).harvestLevel(0)
													.harvestTool(ToolType.SHOVEL),
				new Item.Properties().group(FAItemGroups.metallurgy));
		setDefaultState(stateContainer.getBaseState().with(ACTIVATED, false));
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}

	@Override
	public int tickRate(IWorldReader worldIn)
	{
		return 6000;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
	{
		boolean activated = state.get(ACTIVATED);
		if (activated)
		{
			if (isSurrounded(
					world, pos, n -> n.getBlock() == FABlocks.metalPlateBlock.GetBlock(Metals.COPPER)
							|| n.getBlock() == FABlocks.ironBloom || (n.getBlock() == this && n.get(ACTIVATED))))
				world.setBlockState(pos, FABlocks.ironBloom.ToBlock().getDefaultState());
			else
				world.setBlockState(pos, state.with(ACTIVATED, false));
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
		boolean sidesOnFire = false;
		boolean isSurrounded = true;
		if (!state.get(ACTIVATED))
		{
			if (block.getBlock() == Blocks.FIRE || (block.getBlock() == this && block.get(ACTIVATED)))
			{
				for (Direction face : Direction.values())
				{
					BlockPos offset = pos.offset(face);
					BlockState state1 = world.getBlockState(offset);
					if (state1.getBlock().isAir(state1, world, offset))
					{
						isSurrounded = false;
						world.setBlockState(offset, ((FireBlock) Blocks.FIRE).getStateForPlacement(world, offset));
						sidesOnFire = true;
					} else if (state1.getBlock() == Blocks.FIRE)
					{
						sidesOnFire = true;
					} else if (state1.getBlock() != FABlocks.metalPlateBlock.GetBlock(Metals.COPPER)
							&& state1.getBlock() != this && state1.getBlock() != FABlocks.ironBloom)
					{
						isSurrounded = false;
					}
				}
				if (isSurrounded)
				{
					world.setBlockState(pos, state.with(ACTIVATED, true), 7);
					world.getPendingBlockTicks().scheduleTick(pos, this, tickRate(world));
				}
			}

		} else
		{
			for (Direction face : Direction.values())
			{
				BlockPos offset = pos.offset(face);
				BlockState state1 = world.getBlockState(offset);
				if (state1.getBlock().isAir(state1, world, offset))
				{
					isSurrounded = false;
					world.setBlockState(offset, Blocks.FIRE.getDefaultState());
					sidesOnFire = true;
				} else if (state1.getBlock() == Blocks.FIRE)
				{
					sidesOnFire = true;
				} else if (state1.getBlock() != FABlocks.metalPlateBlock.GetBlock(Metals.COPPER)
						&& state1.getBlock() != this && state1.getBlock() != FABlocks.ironBloom)
				{
					isSurrounded = false;
				}
			}
			if (!sidesOnFire && !isSurrounded)
				world.setBlockState(pos, state.with(ACTIVATED, false));
		}
	}

	@Override
	public boolean isBurning(BlockState state, IBlockReader world, BlockPos pos)
	{
		return state.get(ACTIVATED);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(ACTIVATED);
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
	{
		if (!state.get(ACTIVATED))
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
