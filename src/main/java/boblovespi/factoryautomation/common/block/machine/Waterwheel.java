package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEWaterwheel;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.minecraft.util.EnumFacing.AxisDirection.POSITIVE;

/**
 * Created by Willi on 6/23/2019.
 */
public class Waterwheel extends FABaseBlock
{
	public static final PropertyEnum<Axis> AXIS = PropertyEnum.create("axis", Axis.class, Axis::isHorizontal);
	public static final PropertyBool MULTIBLOCK_COMPLETE = PropertyBool.create("multiblock_complete");

	public Waterwheel()
	{
		super(Materials.WOOD_MACHINE, "waterwheel", FAItemGroups.mechanical);
		setHardness(1f);
		setResistance(10);
		setHarvestLevel("axe", 0);
		TileEntityHandler.tiles.add(TEWaterwheel.class);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, AXIS, MULTIBLOCK_COMPLETE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(AXIS, (meta & 1) == 0 ? Axis.X : Axis.Z)
								.withProperty(MULTIBLOCK_COMPLETE, (meta & 2) == 2);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(AXIS) == Axis.X ? 0 : 1) + (state.getValue(MULTIBLOCK_COMPLETE) ? 2 : 0);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getDefaultState().withProperty(AXIS, placer.getHorizontalFacing().getAxis());
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
		return new TEWaterwheel();
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TEWaterwheel)
			{
				TEWaterwheel waterwheel = (TEWaterwheel) te;
				Axis axis = state.getValue(AXIS);
				if (IsComplete(world, pos, axis))
					waterwheel.CreateStructure();
				else
					waterwheel.BreakStructure();
			}
		}
		return true;
	}

	private boolean MatchesStair(IBlockState state, EnumFacing dir, BlockStairs.EnumHalf half)
	{
		return state.getBlock() == Blocks.OAK_STAIRS && state.getValue(BlockStairs.FACING) == dir.getOpposite()
				&& state.getValue(BlockStairs.HALF) == half;
	}

	private boolean IsComplete(World world, BlockPos pos, Axis axis)
	{
		if (MultiblockHelper
				.IsStructureComplete(world, pos, "waterwheel", EnumFacing.getFacingFromAxis(POSITIVE, axis)))
		{
			EnumFacing back = EnumFacing.getFacingFromAxis(POSITIVE, axis).rotateYCCW();
			EnumFacing front = EnumFacing.getFacingFromAxis(POSITIVE, axis).rotateY();

			if (!MatchesStair(world.getBlockState(pos.offset(back).up(2)), back, BlockStairs.EnumHalf.BOTTOM))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(back, 2).up()), back, BlockStairs.EnumHalf.BOTTOM))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(back).down(2)), back, BlockStairs.EnumHalf.TOP))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(back, 2).down()), back, BlockStairs.EnumHalf.TOP))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(front).up(2)), front, BlockStairs.EnumHalf.BOTTOM))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(front, 2).up()), front, BlockStairs.EnumHalf.BOTTOM))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(front).down(2)), front, BlockStairs.EnumHalf.TOP))
				return false;
			if (!MatchesStair(world.getBlockState(pos.offset(front, 2).down()), front, BlockStairs.EnumHalf.TOP))
				return false;
			return true;
		}
		return false;
	}
}
