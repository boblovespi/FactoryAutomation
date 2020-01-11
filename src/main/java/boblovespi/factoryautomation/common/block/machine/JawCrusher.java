package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEJawCrusher;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 2/17/2018.
 */
public class JawCrusher extends FABaseBlock implements ITileEntityProvider
{
	public static PropertyDirection FACING = BlockHorizontal.FACING;

	public JawCrusher()
	{
		super(Material.CIRCUITS, "jaw_crusher");
		setDefaultState(blockState.getBaseState()
								  .withProperty(FACING, Direction.NORTH));
		TileEntityHandler.tiles.add(TEJawCrusher.class);
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
		return new TEJawCrusher();
	}

	/**
	 * Called when the block is right clicked by a player.
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos,
			BlockState state, PlayerEntity playerIn, EnumHand hand,
			Direction facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
			return true;

		ItemStack held = playerIn.getHeldItem(hand);
		TileEntity te = worldIn.getTileEntity(pos);
		if (held.isEmpty())
		{
			if (te instanceof TEJawCrusher)
				((TEJawCrusher) te).RemovePlate();
		} else
		{
			if (te instanceof TEJawCrusher)
				((TEJawCrusher) te).PlaceWearPlate(held);
		}

		return true;
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
}
