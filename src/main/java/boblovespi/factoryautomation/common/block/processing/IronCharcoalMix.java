package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.world.item.Item;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Predicate;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 2/3/2019.
 */
public class IronCharcoalMix extends FABaseBlock
{
	public static final BooleanProperty ACTIVATED = LogPile.ACTIVATED;

	public IronCharcoalMix()
	{
		super("iron_charcoal_mix", false, Properties.of(Material.STONE).strength(3.5f).harvestLevel(0)
													.harvestTool(ToolType.SHOVEL),
				new Item.Properties().tab(FAItemGroups.metallurgy));
		registerDefaultState(stateDefinition.any().setValue(ACTIVATED, false));
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}

	public int tickRate(LevelReader levelIn)
	{
		return 6000;
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand)
	{
		boolean activated = state.getValue(ACTIVATED);
		if (activated)
		{
			if (isSurrounded(
					world, pos, n -> n.getBlock() == FABlocks.metalPlateBlock.GetBlock(Metals.COPPER)
							|| n.getBlock() == FABlocks.ironBloom || (n.getBlock() == this && n.getValue(ACTIVATED))))
				world.setBlockAndUpdate(pos, FABlocks.ironBloom.ToBlock().defaultBlockState());
			else
				world.setBlockAndUpdate(pos, state.setValue(ACTIVATED, false));
		}
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block1, BlockPos fromPos,
			boolean isMoving)
	{
		BlockState block = world.getBlockState(fromPos);
		boolean sidesOnFire = false;
		boolean isSurrounded = true;
		if (!state.getValue(ACTIVATED))
		{
			if (block.getBlock() == Blocks.FIRE || (block.getBlock() == this && block.getValue(ACTIVATED)))
			{
				for (Direction face : Direction.values())
				{
					BlockPos offset = pos.relative(face);
					BlockState state1 = world.getBlockState(offset);
					if (state1.getBlock().isAir(state1, world, offset))
					{
						isSurrounded = false;
						world.setBlockAndUpdate(offset, FireBlock.getState(world, offset));
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
					world.setBlock(pos, state.setValue(ACTIVATED, true), 7);
					world.getBlockTicks().scheduleTick(pos, this, tickRate(world));
				}
			}

		} else
		{
			for (Direction face : Direction.values())
			{
				BlockPos offset = pos.relative(face);
				BlockState state1 = world.getBlockState(offset);
				if (state1.getBlock().isAir(state1, world, offset))
				{
					isSurrounded = false;
					world.setBlockAndUpdate(offset, Blocks.FIRE.defaultBlockState());
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
				world.setBlockAndUpdate(pos, state.setValue(ACTIVATED, false));
		}
	}

	@Override
	public boolean isBurning(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.getValue(ACTIVATED);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(ACTIVATED);
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand)
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

	private boolean isSurrounded(Level level, BlockPos pos, @Nullable Predicate<BlockState> block)
	{
		for (Direction dir : Direction.values())
		{
			BlockPos offset = pos.relative(dir);
			BlockState state = level.getBlockState(offset);
			if (block == null)
			{
				if (!Block.isFaceFull(state.getShape(level, offset), dir.getOpposite()) && state.getBlock() != this)
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
