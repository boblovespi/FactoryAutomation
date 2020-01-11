package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.api.energy.electricity.IEnergyBlock;
import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEMotor;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 3/19/2018.
 */
public class Motor extends FABaseBlock implements ITileEntityProvider,
		IEnergyBlock
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public Motor()
	{
		super(Material.DRAGON_EGG, "motor", null);
		setDefaultState(blockState.getBaseState()
								  .withProperty(FACING, Direction.NORTH));
		TileEntityHandler.tiles.add(TEMotor.class);
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
		return new TEMotor();
	}

	/**
	 * Checks whether or not a cable can connect to the given side and state
	 *
	 * @param state The state of the machine
	 * @param side  The side power is being connected to
	 * @param world The world access
	 * @param pos   The position of the block
	 * @return Whether or not a cable can attach to the given side and state
	 */
	@Override
	public boolean CanConnectCable(BlockState state, Direction side,
			IBlockAccess world, BlockPos pos)
	{
		return side == state.getValue(FACING);
	}

	@Override
	public BlockState getStateForPlacement(World world, BlockPos pos,
			Direction facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer, EnumHand hand)
	{
		return this.getDefaultState()
				   .withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public BlockState getStateFromMeta(int meta)
	{
		Direction Direction = Direction.getHorizontal(meta & 3);

		if (Direction.getAxis() == Direction.Axis.Y)
		{
			Direction = Direction.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, Direction);
	}

	@Override
	public int getMetaFromState(BlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING);
	}
	@Override
	public boolean isFullBlock(BlockState state)
	{
		return false;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 *
	 * @param state
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
}
