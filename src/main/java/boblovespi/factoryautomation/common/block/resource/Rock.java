package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.SoundHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Willi on 12/26/2018.
 */
public class Rock extends FABaseBlock
{
	public static final PropertyEnum<Variants> VARIANTS = PropertyEnum.create("variants", Variants.class);
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(
			3 / 16d, 0, 3 / 16d, 13 / 16d, 5 / 16d, 13 / 16d);

	public Rock()
	{
		super(Materials.ROCKS, "rock", FAItemGroups.resources, true);
		setDefaultState(getDefaultState().withProperty(VARIANTS, Variants.COBBLESTONE));
		item = new RockItem(this);
		FAItems.items.add(item);
		setSoundType(SoundHandler.rock);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, VARIANTS);
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BOUNDING_BOX;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "resources/" + RegistryName();
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(VARIANTS, Variants.values()[meta]);
	}

	/**
	 * This gets a complete list of items dropped from this block.
	 *
	 * @param drops   add all items this block drops to this drops list
	 * @param world   The current world
	 * @param pos     Block position in world
	 * @param state   Current state
	 * @param fortune Breakers fortune level
	 */
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		super.getDrops(drops, world, pos, state, fortune);
		if (state.getValue(VARIANTS) == Variants.MOSSY_COBBLESTONE)
		{
			Random rand = world instanceof World ? ((World) world).rand : RANDOM;
			if (rand.nextFloat() < 0.4f)
				drops.add(new ItemStack(FAItems.plantFiber.ToItem()));
		}
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(VARIANTS).ordinal();
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
	{
		if (!world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP))
		{
			dropBlockAsItem(world, pos, state, 0);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	/**
	 * Checks if this block can be placed exactly at the given position.
	 */
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return super.canPlaceBlockAt(world, pos) && world.getBlockState(pos.down())
														 .isSideSolid(world, pos.down(), EnumFacing.UP);
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
		public String getName()
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
			super(base);
		}

		@Override
		public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
				EnumFacing facing, float hitX, float hitY, float hitZ)
		{
			if (player.isSneaking() && world.getBlockState(pos).getBlock() instanceof BlockLog)
			{
				if (!world.isRemote)
				{
					int meta = world.getBlockState(pos).getBlock() == Blocks.LOG ?
							world.getBlockState(pos).getValue(BlockOldLog.VARIANT).getMetadata() :
							world.getBlockState(pos).getValue(BlockNewLog.VARIANT).getMetadata();
					world.destroyBlock(pos, false);
					world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(),
							new ItemStack(FABlocks.woodChoppingBlocks.get(meta).ToBlock(), 2)));
				}
				return EnumActionResult.SUCCESS;
			} else
				return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
		}
	}
}
