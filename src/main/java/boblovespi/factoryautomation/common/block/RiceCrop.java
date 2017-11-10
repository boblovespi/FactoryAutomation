package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Willi on 4/11/2017.
 */
public class RiceCrop extends BlockBush implements IGrowable, FABlock
{
	private static final PropertyInteger Age = PropertyInteger
			.create("age", 0, 7);
	private static final AxisAlignedBB BoundingBox = new AxisAlignedBB(0, 0, 0,
			1, 1 / 16, 1);

	public RiceCrop()
	{
		setDefaultState(
				blockState.getBaseState().withProperty(AgeProperty(), 0));
		setTickRandomly(true);

		setHardness(0);
		setSoundType(SoundType.PLANT);
		disableStats();

		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());

		FABlocks.blocks.add(this);
	}

	protected int getAge(IBlockState state)
	{
		return state.getValue(AgeProperty());
	}

	public IBlockState WithAge(int age)
	{
		return getDefaultState().withProperty(this.AgeProperty(), age);
	}

	public int MaxAge()
	{
		return 7;
	}

	protected PropertyInteger AgeProperty()
	{
		return Age;
	}

	@Override public boolean canGrow(World world, BlockPos blockPos,
			IBlockState iBlockState, boolean b)
	{
		return !isMaxAge(iBlockState);
	}

	private boolean isMaxAge(IBlockState state)
	{
		return state.getValue(Age) == MaxAge();
	}

	@Override public boolean canUseBonemeal(World world, Random random,
			BlockPos blockPos, IBlockState iBlockState)
	{
		return true;
	}

	@Override public void grow(World world, Random random, BlockPos blockPos,
			IBlockState iBlockState)
	{
		world.setBlockState(blockPos,
				WithAge(getAge(iBlockState) + 1 > MaxAge() ?
						MaxAge() :
						getAge(iBlockState) + 1), 2);
	}

	@Override protected boolean canSustainBush(IBlockState state)
	{
		return state.getBlock() == Blocks.WATER
				|| state.getBlock() == Blocks.FLOWING_WATER;
	}

	@Override protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, Age);
	}

	@Override public List<ItemStack> getDrops(IBlockAccess blockAccess,
			BlockPos pos, IBlockState state, int i)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();

		if (isMaxAge(state))
		{
			drops.add(new ItemStack(FAItems.riceGrain.ToItem(),
					(int) (Math.random() * 4)));
		}
		drops.add(new ItemStack(FAItems.riceGrain.ToItem()));

		return drops;
	}

	@Override public int getMetaFromState(IBlockState state)
	{
		return state.getValue(AgeProperty());
	}

	@Override public IBlockState getStateFromMeta(int meta)
	{
		return WithAge(meta);
	}

	@Override public String UnlocalizedName()
	{
		return "rice_crop";
	}

	@Override public void updateTick(World worldIn, BlockPos pos,
			IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);

		if (worldIn.getBlockState(pos.down()).getBlock() != Blocks.WATER
				&& worldIn.getBlockState(pos.down()).getBlock()
				!= Blocks.FLOWING_WATER)
		{
			worldIn.setBlockToAir(pos);
			dropBlockAsItem(worldIn, pos, state, 0);
			return;
		}

		if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
		{
			int i = getAge(state);

			if (i < MaxAge())
			{
				float f = GrowthChance(this, worldIn, pos);

				if (ForgeHooks.onCropsGrowPre(worldIn, pos, state,
						rand.nextInt((int) (25.0F / f) + 1) == 0))
				{
					worldIn.setBlockState(pos, WithAge(i + 1), 2);
					ForgeHooks.onCropsGrowPost(worldIn, pos, state,
							worldIn.getBlockState(pos));
				}
			}
		}
	}

	private float GrowthChance(RiceCrop riceCrop, World w, BlockPos pos)
	{
		float chance = 0;

		BlockPos d = pos.down();
		if (w.getBlockState(d).getBlock() == Blocks.WATER
				|| w.getBlockState(d).getBlock() == Blocks.FLOWING_WATER)
			chance += 1;
		else
			return 0;

		for (int x = -2; x < 3; ++x)
		{
			for (int y = -2; y < 3; ++y)
			{
				if (w.getBlockState(d.add(x, 0, y)) == Blocks.WATER)
					chance += 0.5;
				if (w.getBlockState(d.add(x, 0, y)) == Blocks.FLOWING_WATER)

					chance += 0.3;

				if (w.getBlockState(d.add(x, -1, y)) == Blocks.DIRT
						|| w.getBlockState(d.add(x, -1, y)) == Blocks.SAND
						|| w.getBlockState(d.add(x, -1, y)) == Blocks.GRAVEL)
					chance += 0.2;
			}
		}

		return chance;
	}

	@Override public ItemStack getPickBlock(IBlockState state,
			RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player)
	{
		return new ItemStack(FAItems.riceGrain.ToItem());

	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	//	@Override public boolean canSustainPlant(IBlockState state,
	//			IBlockAccess world, BlockPos pos, EnumFacing direction,
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
}
