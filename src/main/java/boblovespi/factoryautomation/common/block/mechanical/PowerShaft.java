package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEPowerShaft;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 1/15/2018.
 */
public class PowerShaft extends FABaseBlock implements ITileEntityProvider
{
	public static PropertyEnum<Axis> AXIS = PropertyEnum.create("facing", Axis.class);
	public static PropertyBool IS_TESR = PropertyBool.create("is_tesr");

	public static AxisAlignedBB[] AABB = new AxisAlignedBB[] {
			new AxisAlignedBB(6.5 / 16d, 0 / 16d, 6.5 / 16d, 6.5 / 16d + 3 / 16d, 16 / 16d, 6.5 / 16d + 3 / 16d),
			new AxisAlignedBB(0 / 16d, 6.5 / 16d, 6.5 / 16d, 16 / 16d, 6.5 / 16d + 3 / 16d, 6.5 / 16d + 3 / 16d),
			new AxisAlignedBB(6.5 / 16d, 6.5 / 16d, 0 / 16d, 6.5 / 16d + 3 / 16d, 6.5 / 16d + 3 / 16d,
					16 / 16d) }; // 0: up; 1: n-s; 2: e-w

	public PowerShaft()
	{
		super(Material.IRON, "power_shaft", null);
		setDefaultState(blockState.getBaseState().withProperty(AXIS, Axis.X).withProperty(IS_TESR, false));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB[GetAxisId(state.getValue(AXIS))];
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
		return new TEPowerShaft();
	}

	private int GetAxisId(Axis a)
	{
		switch (a)
		{
		case Y:
			return 0;
		case X:
			return 1;
		case Z:
			return 2;
		default:
			return 0;
		}
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 *
	 * @param meta
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		switch (meta)
		{
		case 0:
			return getDefaultState().withProperty(AXIS, Axis.Y);
		case 1:
			return getDefaultState().withProperty(AXIS, Axis.X);
		case 2:
			return getDefaultState().withProperty(AXIS, Axis.Z);
		default:
			return getDefaultState();
		}
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 *
	 * @param state
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return GetAxisId(state.getValue(AXIS));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, AXIS, IS_TESR);
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(AXIS, facing.getAxis());
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return this.getDefaultState().withProperty(AXIS, facing.getAxis());
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "mechanical/" + RegistryName();
	}
}
