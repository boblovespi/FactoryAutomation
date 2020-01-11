package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEHandCrank;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 9/3/2018.
 */
public class HandCrank extends FABaseBlock implements ITileEntityProvider
{
	public static final PropertyBool INVERTED = PropertyBool.create("inverted");
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(
			2 / 16d, 0, 2 / 16d, 14 / 16d, 14 / 16d, 14 / 16d);
	private static final AxisAlignedBB BOUNDING_BOX_I = new AxisAlignedBB(
			2 / 16d, 2 / 16d, 2 / 16d, 14 / 16d, 1, 14 / 16d);

	public HandCrank()
	{
		super(Material.WOOD, "hand_crank", FAItemGroups.mechanical);
		TileEntityHandler.tiles.add(TEHandCrank.class);
		setDefaultState(getDefaultState().withProperty(INVERTED, false));
	}

	/**
	 * Gets the {@link BlockState} to place
	 */
	@Override
	public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return facing == Direction.DOWN ? getDefaultState().withProperty(INVERTED, true) : getDefaultState();
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEHandCrank();
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, EnumHand hand,
			Direction facing, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TEHandCrank)
			{
				TEHandCrank crank = (TEHandCrank) tileEntity;

				crank.Rotate();
				player.addExhaustion(0.8f);
			}
		}

		return true;
	}

	@Override
	public boolean isFullCube(BlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(BlockState state)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos)
	{
		return state.getValue(INVERTED) ? BOUNDING_BOX_I : BOUNDING_BOX;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, INVERTED);
	}

	@Override
	public BlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(INVERTED, meta == 1);
	}

	@Override
	public int getMetaFromState(BlockState state)
	{
		return state.getValue(INVERTED) ? 1 : 0;
	}
}
