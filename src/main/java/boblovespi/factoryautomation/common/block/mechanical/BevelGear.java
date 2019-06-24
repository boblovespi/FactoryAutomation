package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEBevelGear;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 6/20/2019.
 */
public class BevelGear extends FABaseBlock
{
	public static PropertyInteger LAYER = PropertyInteger.create("layer", 0, 2);
	public static PropertyDirection FACING = BlockHorizontal.FACING;

	public BevelGear()
	{
		super(Material.IRON, "bevel_gear", FACreativeTabs.mechanical);
		TileEntityHandler.tiles.add(TEBevelGear.class);
	}

	public static EnumFacing GetNegative(IBlockState state)
	{
		if (state.getValue(LAYER) == 1)
			return state.getValue(FACING).rotateY();
		else if (state.getValue(LAYER) == 0)
			return EnumFacing.DOWN;
		else
			return EnumFacing.UP;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, LAYER, FACING);
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TEBevelGear();
	}

	/**
	 * Gets the {@link IBlockState} to place
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
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		IBlockState state = getDefaultState();
		if (facing == EnumFacing.UP)
			state = state.withProperty(LAYER, 0);
		else if (facing == EnumFacing.DOWN)
			state = state.withProperty(LAYER, 1);
		else
			state = state.withProperty(LAYER, hitY > 2 / 3f ? 2 : hitY > 1 / 3f ? 1 : 0);
		if (facing.getHorizontalIndex() >= 0)
			state = state.withProperty(FACING, facing.getOpposite());
		else
			state = state.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
		return state;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(LAYER, meta / 4).withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(LAYER) * 4 + state.getValue(FACING).getHorizontalIndex();
	}
}
