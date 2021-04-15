package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

/**
 * Created by Willi on 4/11/2017.
 */
public class RiceCrop extends BushBlock implements IGrowable, FABlock
{
	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 7);
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(0, 0, 0, 16, 1, 16),
			Block.box(0, 0, 0, 16, 3, 16), Block.box(0, 0, 0, 16, 5, 16),
			Block.box(0, 0, 0, 16, 7, 16), Block.box(0, 0, 0, 16, 9, 16),
			Block.box(0, 0, 0, 16, 11, 16), Block.box(0, 0, 0, 16, 13, 16),
			Block.box(0, 0, 0, 16, 15, 16) };

	public RiceCrop()
	{
		super(Properties.of(Material.PLANTS).strength(0).tickRandomly().sound(SoundType.CROP).doesNotBlockMovement());
		setRegistryName(RegistryName());
		registerDefaultState(stateDefinition.any().with(AGE, 0));
		FABlocks.blocks.add(this);
	}

	protected int getAge(BlockState state)
	{
		return state.getValue(AGE);
	}

	public BlockState WithAge(int age)
	{
		return getDefaultState().with(AGE, age);
	}

	public int MaxAge()
	{
		return 7;
	}

	@Override
	public boolean canGrow(IBlockReader world, BlockPos blockPos, BlockState state, boolean b)
	{
		return !isMaxAge(state);
	}

	private boolean isMaxAge(BlockState state)
	{
		return state.getValue(AGE) == MaxAge();
	}

	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, BlockState BlockState)
	{
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos blockPos, BlockState BlockState)
	{
		world.setBlockState(blockPos, WithAge(Math.min(getAge(BlockState) + 1, MaxAge())), 2);
	}

	protected boolean canSustainBush(BlockState state)
	{
		return state.getFluidState().getFluid() == Fluids.WATER
				|| state.getFluidState().getFluid() == Fluids.FLOWING_WATER;
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return canSustainBush(state);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(AGE);
	}

	// TODO: LOOT TABLES!

	@Override
	public String UnlocalizedName()
	{
		return "rice_crop";
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
	{
		if (!canSustainBush(world.getBlockState(pos.down())))
		{
			world.destroyBlock(pos, true);
			return;
		}

		if (world.getLight(pos.up()) >= 9)
		{
			int i = getAge(state);

			if (i < MaxAge())
			{
				float f = GrowthChance(this, world, pos);

				if (ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt((int) (25.0F / f) + 1) == 0))
				{
					world.setBlockState(pos, WithAge(i + 1), 2);
					ForgeHooks.onCropsGrowPost(world, pos, state);
				}
			}
		}
	}

	private float GrowthChance(RiceCrop riceCrop, World w, BlockPos pos)
	{
		float chance = 0;

		BlockPos d = pos.down();
		if (canSustainBush(w.getBlockState(d)))
			chance += 1;
		else
			return 0;

		for (int x = -2; x < 3; ++x)
		{
			for (int y = -2; y < 3; ++y)
			{
				if (w.getBlockState(d.add(x, 0, y)).getFluidState().getFluid() == Fluids.WATER)
					chance += 0.5;
				if (w.getBlockState(d.add(x, 0, y)).getFluidState().getFluid() == Fluids.FLOWING_WATER)
					chance += 0.3;

				if (w.getBlockState(d.add(x, -1, y)).getBlock() == Blocks.DIRT
						|| w.getBlockState(d.add(x, -1, y)).getBlock() == Blocks.SAND
						|| w.getBlockState(d.add(x, -1, y)).getBlock() == Blocks.GRAVEL)
					chance += 0.2;
			}
		}

		return chance;
	}

	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state)
	{
		return new ItemStack(FAItems.riceGrain.ToItem());
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	//	@Override public boolean canSustainPlant(BlockState state,
	//			IBlockAccess world, BlockPos pos, Direction direction,
	//			IPlantable plantable)
	//	{
	//		if(plantable.getPlant(world, pos) instanceof RiceGrain)
	//			return this
	//	}

	@Override
	public boolean IsItemBlock()
	{
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE_BY_AGE[state.getValue(AGE)];
	}
}
