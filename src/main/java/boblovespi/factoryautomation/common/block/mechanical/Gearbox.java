package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.tools.Wrench;
import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEGearbox;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 5/6/2018.
 */
public class Gearbox extends FABaseBlock implements ITileEntityProvider
{
	public static PropertyDirection FACING = PropertyDirection.create("facing");

	public Gearbox()
	{
		super(Material.IRON, "gearbox", null);
		setDefaultState(blockState.getBaseState().withProperty(FACING, Direction.WEST));
		TileEntityHandler.tiles.add(TEGearbox.class);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 *
	 * @param worldIn
	 * @param meta
	 */
	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEGearbox();
	}

	/**
	 * Gets the {@link BlockState} to place
	 *
	 * @param world  The world the block is being placed in
	 * @param pos    The position the block is being placed at
	 * @param facing The side the block is being placed on
	 * @param hitX   The X coordinate of the hit vector
	 * @param hitY   The Y coordinate of the hit vector
	 * @param hitZ   The Z coordinate of the hit vector
	 * @param meta   The metadata of {@link ItemStack} as processed by {@link Item#getMetadata(int)}
	 * @param placer The entity placing the block
	 * @param hand   The player hand used to place this block
	 * @return The state to be placed in the world
	 */
	@Override
	public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return blockState.getBaseState().withProperty(FACING, facing);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 *
	 * @param state
	 */
	@Override
	public int getMetaFromState(BlockState state)
	{
		return state.getValue(FACING).ordinal();
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 *
	 * @param meta
	 */
	@Override
	public BlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(FACING, Direction.values()[meta]);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING);
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn,
			EnumHand hand, Direction facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);

		if (stack.getItem() instanceof Wrench)
			return false;

		if (worldIn.isRemote)
			return true;

		TileEntity tileEntity = worldIn.getTileEntity(pos);
		Item item = stack.getItem();
		GearType gear = GetGear(item);

		if (gear != null)
		{
			if (tileEntity instanceof TEGearbox)
			{
				TEGearbox te = (TEGearbox) tileEntity;
				if (te.AddGear(gear, gear.durability - stack.getItemDamage()))
					stack.shrink(1);
			}
		} else if (stack.isEmpty())
		{
			if (tileEntity instanceof TEGearbox)
			{
				((TEGearbox) tileEntity).RemoveGear();
			}
		}

		return true;
	}

	private boolean IsGear(Item item)
	{
		for (GearType gearType : GearType.values())
		{
			if (item == FAItems.gear.GetItem(gearType))
				return true;
		}
		return false;
	}

	private GearType GetGear(Item item)
	{
		for (GearType gearType : GearType.values())
		{
			if (item == FAItems.gear.GetItem(gearType))
				return gearType;
		}
		return null;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(BlockState state)
	{
		return false;
	}

	/**
	 * Return true if the block is a normal, solid cube.  This
	 * determines indirect power state, entity ejection from blocks, and a few
	 * others.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 * @return True if the block is a full cube
	 */
	@Override
	public boolean isNormalCube(BlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "mechanical/" + RegistryName();
	}

	public enum GearType implements IStringSerializable, IMultiTypeEnum
	{
		WOOD(1, 40, "wood"),
		STONE(2, 50, "stone"),
		COPPER(1, 200, "copper"),
		IRON(2, 600, "iron"),
		GOLD(3, 400, "gold"),
		BRONZE(5, 600, "bronze"),
		DIAMOND(4, 900, "diamond"),
		MAGMATIC_BRASS(8, 900, "magmatic_brass"),
		STEEL(16, 1200, "steel");

		public final int scaleFactor;
		public final int durability;
		private final String name;

		GearType(int scaleFactor, int durability, String name)
		{
			this.scaleFactor = scaleFactor;
			this.durability = durability;
			this.name = name;
		}

		@Override
		public int GetId()
		{
			return ordinal();
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
}
