package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.ItemLike;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created by Willi on 4/11/2017.
 */
public class RiceCrop extends CropBlock implements FABlock
{
	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 7);
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(0, 0, 0, 16, 1, 16),
			Block.box(0, 0, 0, 16, 3, 16), Block.box(0, 0, 0, 16, 5, 16),
			Block.box(0, 0, 0, 16, 7, 16), Block.box(0, 0, 0, 16, 9, 16),
			Block.box(0, 0, 0, 16, 11, 16), Block.box(0, 0, 0, 16, 13, 16),
			Block.box(0, 0, 0, 16, 15, 16) };

	public RiceCrop()
	{
		super(Properties.of(Material.PLANT).strength(0).randomTicks().sound(SoundType.CROP).noCollission());
		setRegistryName(RegistryName());
		registerDefaultState(stateDefinition.any().setValue(AGE, 0));
		FABlocks.blocks.add(this);
	}

	@Override
	protected ItemLike getBaseSeedId()
	{
		return FAItems.riceGrain;
	}

	public BlockState WithAge(int age)
	{
		return defaultBlockState().setValue(AGE, age);
	}

	@Override
	public void growCrops(Level world, BlockPos blockPos, BlockState BlockState)
	{
		world.setBlock(blockPos, WithAge(Math.min(getAge(BlockState) + 1, getMaxAge())), 2);
	}

	protected boolean canSustainBush(BlockState state)
	{
		return state.getFluidState().getType() == Fluids.WATER
				|| state.getFluidState().getType() == Fluids.FLOWING_WATER;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter levelIn, BlockPos pos)
	{
		return canSustainBush(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
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
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random rand)
	{
		if (!canSustainBush(world.getBlockState(pos.below())))
		{
			world.destroyBlock(pos, true);
			return;
		}

		if (world.getLightEmission(pos.above()) >= 9)
		{
			int i = getAge(state);

			if (i < getMaxAge())
			{
				float f = GrowthChance(this, world, pos);

				if (ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt((int) (25.0F / f) + 1) == 0))
				{
					world.setBlock(pos, WithAge(i + 1), 2);
					ForgeHooks.onCropsGrowPost(world, pos, state);
				}
			}
		}
	}

	private float GrowthChance(RiceCrop riceCrop, Level w, BlockPos pos)
	{
		float chance = 0;

		BlockPos d = pos.below();
		if (canSustainBush(w.getBlockState(d)))
			chance += 1;
		else
			return 0;

		for (int x = -2; x < 3; ++x)
		{
			for (int y = -2; y < 3; ++y)
			{
				if (w.getBlockState(d.offset(x, 0, y)).getFluidState().getType() == Fluids.WATER)
					chance += 0.5;
				if (w.getBlockState(d.offset(x, 0, y)).getFluidState().getType() == Fluids.FLOWING_WATER)
					chance += 0.3;

				if (w.getBlockState(d.offset(x, -1, y)).getBlock() == Blocks.DIRT
						|| w.getBlockState(d.offset(x, -1, y)).getBlock() == Blocks.SAND
						|| w.getBlockState(d.offset(x, -1, y)).getBlock() == Blocks.GRAVEL)
					chance += 0.2;
			}
		}

		return chance;
	}

	@Override
	public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player)
	{
		return new ItemStack(FAItems.riceGrain.ToItem());
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	//	@Override public boolean canSustainPlant(BlockState state,
	//			IBlockAccess level, BlockPos pos, Direction direction,
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
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return SHAPE_BY_AGE[state.getValue(AGE)];
	}
}
