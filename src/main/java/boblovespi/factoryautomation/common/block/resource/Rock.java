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
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

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
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(VARIANTS);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
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
	//	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, BlockState state, int fortune)
	//	{
	//		super.getDrops(drops, world, pos, state, fortune);
	//		if (state.getValue(VARIANTS) == Variants.MOSSY_COBBLESTONE)
	//		{
	//			Random rand = world instanceof World ? ((World) world).rand : RANDOM;
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
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving)
	{
		if (!world.getBlockState(pos.down()).isSolidSide(world, pos.down(), Direction.UP)) // isSideSolid ?
		{
			spawnDrops(state, world, pos);
			world.removeBlock(pos, isMoving);
		}
	}

	/**
	 * Checks if this block can be placed exactly at the given position.
	 */
	@Override
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos)
	{
		return world.getBlockState(pos).getMaterial().isReplaceable() && world.getBlockState(pos.down())
																			  .isSolidSide(world, pos.down(),
																					  Direction.UP)
				&& world.getBlockState(pos).getBlock() != this;
	}

	public enum Variants implements IStringSerializable
	{
		COBBLESTONE("cobblestone"),
		STONE("stone"),
		ANDESITE("andesite"),
		DIORITE("diorite"),
		GRANITE("granite"),
		SANDSTONE("sandstone"),
		MOSSY_COBBLESTONE("mossy_cobblestone");

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
		public ActionResultType useOn(ItemUseContext context)
		{
			World world = context.getWorld();
			BlockPos pos = context.getPos();
			Block block = world.getBlockState(pos).getBlock();
			if (context.func_225518_g_() /*isPlayerSneaking*/ && BlockTags.LOGS.contains(block))
			{
				if (!world.isClientSide)
				{
					world.destroyBlock(pos, false);
					world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
							new ItemStack(FABlocks.woodChoppingBlocks.get(WoodTypes.FromLog(block).Index()).ToBlock(),
									2)));
				}
				return ActionResultType.SUCCESS;
			} else
				return super.useOn(context);
		}
	}
}
