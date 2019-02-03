package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Willi on 2/2/2019.
 */
public class BrickMaker extends FABaseBlock
{
	public static PropertyEnum<Contents> CONTENTS = PropertyEnum.create("contents", Contents.class);

	public BrickMaker()
	{
		super(Material.WOOD, "brick_maker_frame", FACreativeTabs.primitive);
		setDefaultState(getDefaultState().withProperty(CONTENTS, Contents.EMPTY));
		setHardness(2.0f);
		setHarvestLevel("axe", 0);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}

	@Override
	public int tickRate(World worldIn)
	{
		return 100;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		Contents value = state.getValue(CONTENTS);
		if (value.CanDry())
		{
			world.setBlockState(pos, state.withProperty(CONTENTS, value.GetDried()));
			if (value.GetDried().CanDry())
				world.scheduleUpdate(pos, this, tickRate(world));
		}
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
			return true;

		Contents value = state.getValue(CONTENTS);
		if (value.CanAddClay() && player.getHeldItem(hand).getItem() == FAItems.terraclay)
		{
			world.setBlockState(pos, state.withProperty(CONTENTS, value.AddClay()));
			player.getHeldItem(hand).shrink(1);
			world.scheduleUpdate(pos, this, tickRate(world));
		} else if (value.CanRemove())
		{
			if (value.CanRemoveBrick())
			{
				world.setBlockState(pos, state.withProperty(CONTENTS, value.RemoveClay()));
				ItemHelper.PutItemsInInventoryOrDrop(player, new ItemStack(FAItems.terraclayBrick.ToItem()), world);

			} else if (value.CanRemoveClay())
			{
				world.setBlockState(pos, state.withProperty(CONTENTS, value.RemoveClay()));
				ItemHelper.PutItemsInInventoryOrDrop(player, new ItemStack(FAItems.terraclay.ToItem()), world);
			}
		}
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(CONTENTS, Contents.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(CONTENTS).ordinal();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, CONTENTS);
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

	public enum Contents implements IStringSerializable
	{
		EMPTY("empty"),
		HALF("half"),
		HALF_DRIED("half_dried"),
		FULL("full"),
		FULL_HALF_DRIED("full_half_dried"),
		FULL_DRIED("full_dried");

		private String name;

		Contents(String name)
		{
			this.name = name;
		}

		@Override
		public String getName()
		{
			return name;
		}

		public Contents GetDried()
		{
			switch (this)
			{
			case EMPTY:
				return EMPTY;
			case HALF:
				return HALF_DRIED;
			case HALF_DRIED:
				return HALF_DRIED;
			case FULL:
				return FULL_HALF_DRIED;
			case FULL_HALF_DRIED:
				return FULL_DRIED;
			case FULL_DRIED:
				return FULL_DRIED;
			default:
				return this;
			}
		}

		public Contents AddClay()
		{
			switch (this)
			{
			case EMPTY:
				return HALF;
			case HALF:
				return FULL;
			case HALF_DRIED:
				return FULL_HALF_DRIED;
			case FULL:
				return this;
			case FULL_HALF_DRIED:
				return this;
			case FULL_DRIED:
				return this;
			default:
				return this;
			}
		}

		public boolean CanDry()
		{
			return this != EMPTY && this != HALF_DRIED && this != FULL_DRIED;
		}

		public boolean CanAddClay()
		{
			return this == EMPTY || this == HALF_DRIED || this == HALF;
		}

		public boolean CanRemoveClay()
		{
			return CanRemove() && this == HALF || this == FULL;
		}

		public boolean CanRemoveBrick()
		{
			return CanRemove() && !CanRemoveClay();
		}

		public boolean CanRemove()
		{
			return this != EMPTY;
		}

		public Contents RemoveClay()
		{
			switch (this)
			{
			case EMPTY:
				return EMPTY;
			case HALF:
				return EMPTY;
			case HALF_DRIED:
				return EMPTY;
			case FULL:
				return HALF;
			case FULL_HALF_DRIED:
				return HALF;
			case FULL_DRIED:
				return HALF_DRIED;
			default:
				return this;
			}
		}
	}
}
