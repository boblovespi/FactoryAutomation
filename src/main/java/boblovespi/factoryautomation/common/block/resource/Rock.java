package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.WoodTypes;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.SoundHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 12/26/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class Rock extends FABaseBlock
{
	public static final EnumProperty<Variants> VARIANTS = EnumProperty.create("variants", Variants.class);
	private static final VoxelShape BOUNDING_BOX = Block.box(3, 0, 3, 13, 5, 13);

	public Rock()
	{
		super("rock", true, Properties.of(Materials.ROCKS).sound(SoundHandler.rock), new Item.Properties());
		// super(Materials.ROCKS, "rock", FAItemGroups.resources, true);
		registerDefaultState(stateDefinition.any().setValue(VARIANTS, Variants.COBBLESTONE));
		item = new RockItem(this);
		FAItems.items.add(item);
		// setSoundType(SoundHandler.rock);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(VARIANTS);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return BOUNDING_BOX;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "resources/" + RegistryName();
	}

	// TODO: loot tables
	//	/**
	//	 * This gets a complete list of items dropped from this block.
	//	 */
	//	@Override
	//	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess level, BlockPos pos, BlockState state, int fortune)
	//	{
	//		super.getDrops(drops, level, pos, state, fortune);
	//		if (state.getValue(VARIANTS) == Variants.MOSSY_COBBLESTONE)
	//		{
	//			Random rand = level instanceof World ? ((World) level).rand : RANDOM;
	//			if (rand.nextFloat() < 0.4f)
	//				drops.add(new ItemStack(FAItems.plantFiber.ToItem()));
	//		}
	//	}

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

	public enum Variants implements StringRepresentable
	{
		COBBLESTONE("cobblestone"),
		STONE("stone"),
		ANDESITE("andesite"),
		DIORITE("diorite"),
		GRANITE("granite"),
		TUFF("tuff"),
		CALCITE("calcite"),
		SANDSTONE("sandstone"),
		MOSSY_COBBLESTONE("mossy_cobblestone"),
		TERRACOTTA("terracotta");

		private final String name;

		Variants(String name)
		{
			this.name = name;
		}

		@Override
		public String getSerializedName()
		{
			return name;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}

	public static class RockItem extends FAItemBlock
	{
		public RockItem(FABlock base)
		{
			super(base, new Item.Properties().tab(FAItemGroups.resources));
		}

		@Override
		public InteractionResult useOn(UseOnContext context)
		{
			Level world = context.getLevel();
			BlockPos pos = context.getClickedPos();
			Block block = world.getBlockState(pos).getBlock();
			if (context.isSecondaryUseActive() /*isPlayerSneaking*/ && BlockTags.LOGS.contains(block))
			{
				if (!world.isClientSide)
				{
					world.destroyBlock(pos, false);
					world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
							new ItemStack(FABlocks.woodChoppingBlocks.get(WoodTypes.FromLog(block).Index()).ToBlock(),
									2)));
				}
				return InteractionResult.SUCCESS;
			} else
				return super.useOn(context);
		}
	}
}
